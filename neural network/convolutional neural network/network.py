'''
Author: Changling Li
Name: network.py
Date: 11/5/20
Represents  a neural network (collection of layers)
'''

import time
import numpy as np

import layer
import optimizer
import accelerated_layer

np.random.seed(0)


class Network():
    '''Represents a neural network with some number of layers of various types.
    '''
    def __init__(self, reg=0, verbose=True):
        # Python list of Layer object references that make up out network
        self.layers = []
        # Regularization strength
        self.reg = reg
        # Whether we want network-related debug/info print outs
        self.verbose = verbose

        # Python list of ints. These are the indices of layers in `self.layers`
        # that have network weights. This should be all types of layers
        # EXCEPT MaxPool2D
        self.wt_layer_inds = []

        # As in former projects, Python list of loss, training/validation
        # accuracy during training recorded at some frequency
        self.loss_history = []
        self.train_acc_history = []
        self.validation_acc_history = []

    def compile(self, optimizer_name, **kwargs):
        '''Tells each network layer how weights should be updated during backprop
        during training 

        Parameters:
        -----------
        optimizer_name: string. Name of optimizer class to use to update wts.
            See optimizer::create_optimizer for specific ones supported.
        **kwargs: Any number of optional parameters that get passed to the
            optimizer of your choice. 
        '''
        # Only set an optimizer for each layer with weights
        for l in [self.layers[i] for i in self.wt_layer_inds]:
            l.compile(optimizer_name, **kwargs)

    def fit(self, x_train, y_train, x_validate, y_validate, mini_batch_sz=100, n_epochs=10,
            acc_freq=10, print_every=50):
        '''Trains the neural network on data

        Parameters:
        -----------
        x_train: ndarray. shape=(num training samples, n_chans, img_y, img_x).
            Training data.
        y_train: ndarray. shape=(num training samples,).
            Training data classes, int coded.
        x_validate: ndarray. shape=(num validation samples, n_chans, img_y, img_x).
        y_validate: ndarray. shape=(num validation samples,).
            Validation data classes, int coded.
        mini_batch_sz: int. Mini-batch training size.
        n_epochs: int. Number of training epochs.
        print_every: int.
            Controls the frequency (in iterations) with which to wait before printing out the loss
            and iteration number.
        acc_freq: int. Should be equal to or a multiple of `print_every`

        '''
        num_samps, n_chans, img_y, img_x = x_train.shape 

        if mini_batch_sz>0:
            iterations = (num_samps//mini_batch_sz)
            if iterations ==0:
                iterations=1
        else:
            iterations = 1

        loss_history = []
        train_acc_history = []
        validation_acc_history = []
        print('Starting to train...')
        print(f'{n_epochs*iterations} iterations. {iterations} iter/epoch.')

        started = False
        start_time = time.time()
        for i in range(n_epochs*iterations):
        	mini_batch_indices = np.random.choice(num_samps,mini_batch_sz, replace = True)
        	mini_batch_true_class = y_train[mini_batch_indices].reshape(mini_batch_sz)
        	mini_batch = x_train[mini_batch_indices,:,:,:]

            #forward
        	loss = self.forward(mini_batch, mini_batch_true_class)
        	loss_history.append(loss)

            #backward
        	self.backward(mini_batch_true_class)

        	for layer in self.layers:
        		layer.update_weights()

        	if i%print_every ==0:
        		print("Completed iteration", i, '/', n_epochs*iterations, "Training loss: ", loss)

        	if started and i%acc_freq==0:
        		train_accuracy = self.accuracy(mini_batch, mini_batch_true_class)
        		train_acc_history.append(train_accuracy)

        		validation_accuracy = self.accuracy(x_validate,y_validate)
        		validation_acc_history.append(validation_accuracy)
        	if i==0:
        		started = True
        		elapsed_time = time.time() - start_time
        		print("Runtime for the first iteration: ", elapsed_time/60, "mins")
        		print("Total runtime projected to be: ", (elapsed_time*n_epochs*iterations)/60, "mins")
        return loss_history, train_acc_history, validation_acc_history

    def predict(self, inputs):
        '''Classifies novel inputs presented to the network using the current
        weights.

        Parameters:
        -----------
        inputs: ndarray. shape=shape=(num test samples, n_chans, img_y, img_x)
            This is test data.

        Returns:
        -----------
        pred_classes: ndarray. shape=shape=(num test samples)
            Predicted classes (int coded) derived from the network.
        '''
        # Do a forward sweep through the network
        prev_inputs = inputs
        for layer in self.layers:
            prev_inputs = layer.forward(prev_inputs)

        pred_classes = np.argmax(prev_inputs, axis=1)
       	return pred_classes

    def accuracy(self, inputs, y, samp_sz=500, mini_batch_sz=15):
        '''Computes accuracy using current net on the inputs `inputs` with classes `y`.

        This method is pre-filled for you (shouldn't require modification).

        Parameters:
        -----------
        inputs: ndarray. shape=shape=(num samples, n_chans, img_y, img_x)
        y: ndarray. int-coded class assignments of training mini-batch. 0,...,numClasses-1
            shape=(N,) for mini-batch size N.
        samp_sz: int.
        mini_batch_sz: Because it might be tricky to hold all the training
            instances in memory at once, process and evaluate the accuracy of
            samples from `input` in mini-batches. We merge the accuracy scores
            across batches so the result is no different than processing all at
            once.
        '''
        n_samps = len(inputs)

        # Do we subsample the input?
        if n_samps > samp_sz:
            subsamp_inds = np.random.choice(n_samps, samp_sz)
            n_samps = samp_sz
            inputs = inputs[subsamp_inds]
            y = y[subsamp_inds]

        # How many mini-batches do we split the data into to test accuracy?
        n_batches = int(np.ceil(n_samps / mini_batch_sz))
        # Placeholder for our predicted class ints
        y_pred = np.zeros(len(inputs), dtype=np.int32)

        # Compute the accuracy through the `predict` method in batches.
        # Strategy is to use a 1D cursor `b` to extract a range of inputs to
        # process (a mini-batch)
        for b in range(n_batches):
            low = b*mini_batch_sz
            high = b*mini_batch_sz+mini_batch_sz
            # Tolerate out-of-bounds as we reach the end of the num samples
            if high > n_samps:
                high = n_samps

            # Get the network predicted classes and put them in the right place
            y_pred[low:high] = self.predict(inputs[low:high])

        # Accuracy is the average proportion that the prediction matchs the true
        # int class
        acc = np.mean(y_pred == y)

        return acc

    def forward(self, inputs, y):
        '''Do forward pass through whole network

        Parameters:
        -----------
        inputs: ndarray. Inputs coming into the input layer of the net. shape=(B, n_chans, img_y, img_x)
        y: ndarray. int-coded class assignments of training mini-batch. 0,...,numClasses-1
            shape=(B,) for mini-batch size B.

        Returns:
        -----------
        loss: float. REGULARIZED loss.
        '''
        prev_inputs = inputs
        for layer in self.layers:
            prev_inputs = layer.forward(prev_inputs)
        last_loss = self.layers[-1].loss(y)
        wt_reg = self.wt_reg_reduce()
        loss = last_loss + wt_reg

        return loss

    def wt_reg_reduce(self):
        '''Computes the loss weight regularization for all network layers that have weights

        Returns:
        -----------
        wt_reg: float. Regularization for weights from all layers across the network.
        '''
        wt_reg =0
        for index in self.wt_layer_inds:
            wt_reg += np.sum(self.reg*(self.layers[index].wts**2))/2
        return wt_reg

    def backward(self, y):
        '''Initiates the backward pass through all the layers of the network.

        Parameters:
        -----------
        y: ndarray. int-coded class assignments of training mini-batch. 0,...,numClasses-1
            shape=(B,) for mini-batch size B.

        Returns:
        -----------
        None
        '''
        d_upstream = None
        d_wts = None
        d_b = None
        for layer in reversed(self.layers):

        	d_upstream,_,_ = layer.backward(d_upstream, y)


class ConvNet4(Network):
    '''
    Makes a ConvNet4 network with the following layers: Conv2D -> MaxPooling2D -> Dense -> Dense

    1. Convolution (net-in), Relu (net-act).
    2. Max pool 2D (net-in), linear (net-act).
    3. Dense (net-in), Relu (net-act).
    4. Dense (net-in), soft-max (net-act).
    '''
    def __init__(self, input_shape=(3, 32, 32), n_kers=(32,), ker_sz=(7,), dense_interior_units=(100,),
                 pooling_sizes=(2,), pooling_strides=(2,), n_classes=10, wt_scale=1e-3, reg=0, verbose=True):
        '''
        Parameters:
        -----------
        input_shape: tuple. Shape of a SINGLE input sample (no mini-batch). By default: (n_chans, img_y, img_x)
        n_kers: tuple. Number of kernels/units in the 1st convolution layer. Format is (32,), which is a tuple
            rather than just an int. The reasoning is that if you wanted to create another Conv2D layer, say with 16
            units, n_kers would then be (32, 16). Thus, this format easily allows us to make the net deeper.
        ker_sz: tuple. x/y size of each convolution filter.
        dense_interior_units: tuple. Number of hidden units in each dense layer. Same format as above.
        pooling_sizes: tuple. Pooling extent in the i-th MaxPooling2D layer.  Same format as above.
        pooling_strides: tuple. Pooling stride in the i-th MaxPooling2D layer.  Same format as above.
        n_classes: int. Number of classes in the input. This will become the number of units in the Output Dense layer.
        wt_scale: float. Global weight scaling to use for all layers with weights
        reg: float. Regularization strength
        verbose: bool. 
        '''
        super().__init__(reg, verbose)

        n_chans, h, w = input_shape

        # 1) Input convolutional layer
        convLayer = layer.Conv2D(0, 'Conv2D', n_kers[0], ker_sz[0], n_chans, wt_scale, 'relu', reg, verbose)
        self.layers.append(convLayer)
        # 2) 2x2 max pooling layer
        poolingLayer = layer.MaxPooling2D(1, 'MaxPooling2D', pooling_sizes[0], pooling_strides[0], 'linear', reg, verbose)
        self.layers.append(poolingLayer)
        y = int((h - pooling_strides[0])/pooling_strides[0])+1
        x = int((w-pooling_strides[0])/pooling_strides[0])+1
        # print(x)
        # print(y)
        # 3) Dense layer
        denseLayer = layer.Dense(2, 'Dense', dense_interior_units[0], n_kers[0]*x*y, wt_scale, 'relu', reg, verbose)
        self.layers.append(denseLayer)
        # 4) Dense softmax output layer
        denseSoft = layer.Dense(3, 'denseSoft', n_classes, dense_interior_units[0], wt_scale, 'softmax', reg, verbose)
        self.layers.append(denseSoft)
        # self.wt_layer_inds = ???
        self.wt_layer_inds = [0,2,3]

class ConvNet4Accel(Network):
    '''
    Makes a ConvNet4 network with the following layers: Conv2D -> MaxPooling2D -> Dense -> Dense

    1. Convolution (net-in), Relu (net-act).
    2. Max pool 2D (net-in), linear (net-act).
    3. Dense (net-in), Relu (net-act).
    4. Dense (net-in), soft-max (net-act).
    '''
    def __init__(self, input_shape=(3, 32, 32), n_kers=(32,), ker_sz=(7,), dense_interior_units=(100,),
                 pooling_sizes=(2,), pooling_strides=(2,), n_classes=10, wt_scale=1e-3, reg=0, verbose=True):
        '''
        Parameters:
        -----------
        input_shape: tuple. Shape of a SINGLE input sample (no mini-batch). By default: (n_chans, img_y, img_x)
        n_kers: tuple. Number of kernels/units in the 1st convolution layer. Format is (32,), which is a tuple
            rather than just an int. The reasoning is that if you wanted to create another Conv2D layer, say with 16
            units, n_kers would then be (32, 16). Thus, this format easily allows us to make the net deeper.
        ker_sz: tuple. x/y size of each convolution filter. 
        dense_interior_units: tuple. Number of hidden units in each dense layer. Same format as above.
            NOTE: Does NOT include the output layer, which has # units = # classes.
        pooling_sizes: tuple. Pooling extent in the i-th MaxPooling2D layer.  Same format as above.
        pooling_strides: tuple. Pooling stride in the i-th MaxPooling2D layer.  Same format as above.
        n_classes: int. Number of classes in the input. This will become the number of units in the Output Dense layer.
        wt_scale: float. Global weight scaling to use for all layers with weights
        reg: float. Regularization strength
        verbose: bool. 
        '''
        super().__init__(reg, verbose)

        n_chans, h, w = input_shape

        # 1) Input convolutional layer
        convLayer = accelerated_layer.Conv2DAccel(0, 'Conv2D', n_kers[0], ker_sz[0], n_chans, wt_scale, 'relu', reg, verbose)
        self.layers.append(convLayer)
        # 2) 2x2 max pooling layer
        poolingLayer = accelerated_layer.MaxPooling2DAccel(1, 'MaxPooling2D', pooling_sizes[0], pooling_strides[0], 'linear', reg, verbose)
        self.layers.append(poolingLayer)
        y = int((h - pooling_strides[0])/pooling_strides[0])+1
        x = int((w-pooling_strides[0])/pooling_strides[0])+1
        # print(x)
        # print(y)
        # 3) Dense layer
        denseLayer = layer.Dense(2, 'Dense', dense_interior_units[0], n_kers[0]*x*y, wt_scale, 'relu', reg, verbose)
        self.layers.append(denseLayer)
        # 4) Dense softmax output layer
        denseSoft = layer.Dense(3, 'denseSoft', n_classes, dense_interior_units[0], wt_scale, 'softmax', reg, verbose)
        self.layers.append(denseSoft)
        # self.wt_layer_inds = ???
        self.wt_layer_inds = [0,2,3]

class NewConvNet4Accel(Network):
    '''
    add one more layer of conv2D, Maxpooling2D and another dense layer
    '''
    def __init__(self, input_shape=(3, 32, 32), n_kers=(32,64), ker_sz=(7,), dense_interior_units=(100,150),
                 pooling_sizes=(2,), pooling_strides=(2,), n_classes=10, wt_scale=1e-3, reg=0, verbose=True):
        '''
        Parameters:
        -----------
        input_shape: tuple. Shape of a SINGLE input sample (no mini-batch). By default: (n_chans, img_y, img_x)
        n_kers: tuple. Number of kernels/units in the 1st convolution layer. Format is (32,), which is a tuple
            rather than just an int. The reasoning is that if you wanted to create another Conv2D layer, say with 16
            units, n_kers would then be (32, 16). Thus, this format easily allows us to make the net deeper.
        ker_sz: tuple. x/y size of each convolution filter.
        dense_interior_units: tuple. Number of hidden units in each dense layer. Same format as above.
        pooling_sizes: tuple. Pooling extent in the i-th MaxPooling2D layer.  Same format as above.
        pooling_strides: tuple. Pooling stride in the i-th MaxPooling2D layer.  Same format as above.
        n_classes: int. Number of classes in the input. This will become the number of units in the Output Dense layer.
        wt_scale: float. Global weight scaling to use for all layers with weights
        reg: float. Regularization strength
        verbose: bool. 
        '''
        super().__init__(reg, verbose)

        n_chans, h, w = input_shape

        # 1) Input convolutional layer
        convLayer = accelerated_layer.Conv2DAccel(0, 'Conv2D', n_kers[0], ker_sz[0], n_chans, wt_scale, 'relu', reg, verbose)
        self.layers.append(convLayer)
        # 2) 2x2 max pooling layer
        poolingLayer = accelerated_layer.MaxPooling2DAccel(1, 'MaxPooling2D', pooling_sizes[0], pooling_strides[0], 'linear', reg, verbose)
        self.layers.append(poolingLayer)
        y = int((h - pooling_strides[0])/pooling_strides[0])+1
        x = int((w-pooling_strides[0])/pooling_strides[0])+1
        # 3) second convolutional layer
        convLayer2 = accelerated_layer.Conv2DAccel(2, 'Conv2D', n_kers[1], ker_sz[0], n_kers[0], wt_scale, 'relu', reg, verbose)
        self.layers.append(convLayer2)
        # 4) second max pooling layer
        poolingLayer2 = accelerated_layer.MaxPooling2DAccel(3, 'MaxPooling2D', pooling_sizes[0], pooling_strides[0], 'linear', reg, verbose)
        self.layers.append(poolingLayer2)
        y = int((y - pooling_strides[0])/pooling_strides[0])+1
        x = int((x-pooling_strides[0])/pooling_strides[0])+1
        # 5) Dense layer
        denseLayer = layer.Dense(4, 'Dense', dense_interior_units[0], n_kers[1]*x*y, wt_scale, 'relu', reg, verbose)
        self.layers.append(denseLayer)
        # 6) Dense layer
        denseLayer2 = layer.Dense(5, 'Dense', dense_interior_units[1], dense_interior_units[0], wt_scale, 'relu', reg, verbose)
        self.layers.append(denseLayer2)
        # 7) Dense softmax output layer
        denseSoft = layer.Dense(6, 'denseSoft', n_classes, dense_interior_units[1], wt_scale, 'softmax', reg, verbose)
        self.layers.append(denseSoft)
        # self.wt_layer_inds = ???
        self.wt_layer_inds = [0,2,4,5,6]

