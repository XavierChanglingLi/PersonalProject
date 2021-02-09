'''
Author: Changling Li
Name: deep_dream.py
Date: 11/20/20
Core functions used in the DeepDream algorithm. Implemented in TensorFlow
'''

import tensorflow as tf
import numpy as np


class DeepDream():
    def __init__(self, net, selected_layer_inds, all_layer_names, filter_inds=[]):
        '''
        Parameters:
        -----------
        net: TensorFlow Keras Model object. Network configured to return netAct values when
            presented an input image.
        selected_layer_inds: Python list of ints. 
        all_layer_names: Python list of strings. Names of ALL non-input layers in `net` (not just selected).
        filter_inds: Python list of ints.
            Determines whether we take netAct values from particular neurons or across the entire layer.
            For usage, see note in forward() docstring.
        '''
        # Tf model
        self.net = net

        # Indices of layer of the net we are interested in focusing on netAct valuees
        self.selected_layer_inds = selected_layer_inds

        # Names of all non-input layers of net
        self.all_layer_names = all_layer_names

        # Indices of filters within each layer which we want to only take their netAct values
        self.filter_inds = filter_inds

    def forward(self, img_tf, verbose=False):
        '''Computes forward pass of `net` with the input `img_tf` to get the mean netAct values at the
        network layers that have indicies `selected_layer_inds`
        OR netAct values of specific filters that have indices `filter_inds`.

        Parameters:
        -----------
        img_tf: tf.Variable. shape=(img_y, img_x, n_chans). Input image
        verbose: Boolean. If true, print debug information.

        Returns:
        -----------
        netActs: Python list of floats, one per network layer. len(netActs) == len(selected_layer_inds)
            netAct values are averaged over each layer OR averaged over specific neurons within a layer
            with indicies `filter_inds`
        '''
        img_tf = tf.expand_dims(img_tf, axis=0)
        netActs = []
        act = self.net(img_tf)
        selectedAct = [act[i] for i in self.selected_layer_inds]
        if verbose == True:
            selectedLayerName = [self.all_layer_names[i] for i in self.selected_layer_inds]
            print(selectedLayerName)
        for tensor in selectedAct:
            if not self.filter_inds:#if the list is empty
                netActs.append(tf.reduce_mean(tensor))
            else:
                selectedTensor = tf.gather(tensor, self.filter_inds, axis=3)
                netActs.append(tf.reduce_mean(selectedTensor))
        return netActs

    def image_gradient(self, img_tf, eps=1e-8, normalized=True, verbose=False):
        '''Computes the (normalized) gradients for each selected network layer with respect to
        the input image `img_tf`.

        Parameters:
        -----------
        img_tf: tf.Variable. shape=(img_y, img_x, n_chans). Input image
        eps: float. Small number used in normalization to prevent divide-by-zero errors
        normalized: boolean. Do we normalize each gradient within each layer by its standard deviation?
        verbose: Boolean. If true, print debug information.

        Returns:
        -----------
        grads: Python list of image gradients [shape=(img_y, img_x, n_chans)], one per selected
            network layer. len(list) = len(selected_layer_inds)
        '''

        with tf.GradientTape(persistent=True) as tape:
            tape.watch(img_tf)
            netActs = self.forward(img_tf)

        grads = []
        for i in range(len(netActs)):
            grad = tape.gradient(netActs[i], img_tf)
            grads.append(grad)
        # print(grads)
        if normalized == True:
            grads = [(grad-tf.math.reduce_mean(grad))/(tf.math.reduce_std(grad)+eps) for grad in grads]

            # grads = [tf.math.divide(grad,tf.sqrt(tf.math.reduce_mean(tf.pow(grad,2)))+eps) for grad in grads]

        return grads

    def gradient_ascent(self, img_tf, n_iter=10, step_sz=0.01, clip_low=0, clip_high=1, verbose=False):
        '''Performs gradient ascent on the input img `img_tf`.
        The function computes the layer gradients respect to `img_tf`, then adds a fraction (`step_sz`) of
        each layer's gradient back to the input image. For efficiency, use `assign_add`.


        Parameters:
        -----------
        img_tf: tf.Variable. shape=(img_y, img_x, n_chans). Input image
        n_iter: int. Number of times to compute layer gradients and add them back to the image.
        step_sz: float. Proportion of each layer gradient to add to the image on each update.
        clip_low: int. Before returning, values below this value are replaced with this thresholded value
        clip_high: int. Before returning, values above this value are replaced with this thresholded value
        verbose: Boolean. If true, print debug information.

        Returns:
        -----------
        img_tf: tf.Variable. shape=(img_y, img_x, n_chans). Input image modified via gradient ascent.
        '''
        for n in range(n_iter):
            grads = self.image_gradient(img_tf)
            for grad in grads:
                img_tf = img_tf+grad*step_sz
            img_tf = tf.clip_by_value(img_tf,clip_low, clip_high)
        return img_tf


    def gradient_ascent_multiscale(self, img_tf, n_scales=4, scale_factor=1.3, n_iter=10, step_sz=0.01,
                                   clip_low=-1, clip_high=2, verbose=False):
        '''Multi-scale version of gradient ascent algorithm.
        Runs gradient ascent on the input image `img_tf`, `n_iter` times, with gradient step size `step_sz`.
        Then we multiplicatively enlarge the image by a factor of `scale_factor` and run gradient
        ascent again (if `n_scales` > 1). The resizing process happens a total of `n_scales` times.


        Parameters:
        -----------
        img_tf: tf.Variable. shape=(img_y, img_x, n_chans). Input image
        n_scales: int. Number of times to resize the input image after running gradient ascent algorithm.
        scale_factor: float. Factor multiplied to current size of image to resize it.
        n_iter: int. Number of times to compute layer gradients and add them back to the image.
        step_sz: float. Proportion of each layer gradient to add to the image on each update.
        clip_low: int. Before returning, values below this value are replaced with this thresholded value
        clip_high: int. Before returning, values above this value are replaced with this thresholded value
        verbose: Boolean. If true, print debug information.

        Returns:
        -----------
        img_tf: tf.Variable. shape=(img_y, img_x, n_chans). Input image modified via gradient ascent.
        '''
        img_tf = self.gradient_ascent(img_tf, n_iter=n_iter, step_sz=step_sz, clip_low=clip_low, clip_high=clip_high, verbose=verbose)
        if n_scales > 1:
            for i in range(n_scales):
                new_size = tf.cast(tf.convert_to_tensor(img_tf.shape[:-1]), tf.float32)*scale_factor
                img_tf = tf.Variable(tf.image.resize(img_tf,tf.cast(new_size, tf.int32)))
                # img_tf = tf.Variable(tf.image.resize(img_tf,img_tf.shape*scale_factor))
                img_tf = self.gradient_ascent(img_tf, n_iter=n_iter, step_sz=step_sz, clip_low=clip_low, clip_high=clip_high, verbose=verbose)
                print("Finished the ", i, "th iteration")
        return img_tf
        

    def tf2array(self, tf_img):
        '''Converts a tf.Variable -> ndarray for plotting via matplotlib.

        Parameters:
        -----------
        img_tf: tf.Variable. shape=(img_y, img_x, n_chans). Deep Dream image

        Returns:
        -----------
        img: ndarray. shape=(img_y, img_x, n_chans). Input image modified for visualization.
        '''
        tf_img = tf.clip_by_value(tf_img,0,1)
        tf_img = tf.squeeze(tf_img)
        tf_img = tf_img.numpy()
        tf_img = (tf_img-np.min(tf_img))/(np.max(tf_img)-np.min(tf_img))
        return tf_img

