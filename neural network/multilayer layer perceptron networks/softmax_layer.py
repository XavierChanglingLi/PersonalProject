'''
Author: Changling Li
Name: softmax_layer.py
Date: 10/20/20
Constructs, trains, tests single layer neural network with softmax activation function.
'''

import numpy as np


class SoftmaxLayer():
    def __init__(self, num_output_units):
        '''
        Parameters:
        -----------
        num_output_units: int. Num output units. Equal to # data classes.
        '''
        # Network weights
        self.wts = None
        # Bias
        self.b = None
        # Number of data classes C
        self.num_output_units = num_output_units

    def accuracy(self, y, y_pred):
        '''
        Parameters:
        -----------
        y: ndarray. int-coded true classes. shape=(Num samps,)
        y_pred: ndarray. int-coded predicted classes by the network. shape=(Num samps,)

        Returns:
        -----------
        float. accuracy in range [0, 1]
        '''

        accuracy = len(np.where(y==y_pred)[0])/len(y)
        return accuracy

    def net_in(self, features):
        '''Computes the net input (net weighted sum)
        Parameters:
        -----------
        features: ndarray. input data. shape=(num images (in mini-batch), num features)
        Returns:
        -----------
        net_input: ndarray. shape=(N, C)
        '''
        net_input = features@self.wts+self.b
        return net_input

    def one_hot(self, y, num_classes):
        '''One-hot codes the output classes for a mini-batch
        Parameters:
        -----------
        y: ndarray. int-coded class assignments of training mini-batch. 0,...,C-1
        num_classes: int. Number of unique output classes total

        Returns:
        -----------
        y_one_hot: One-hot coded class assignments.
        '''

        y_one_hot = np.zeros((y.shape[0],num_classes))
        for i in range(num_classes):
            y_one_hot[:,i]=np.where(y==i,1,0)
        return y_one_hot

    def fit(self, features, y, n_epochs=10000, lr=0.0001, mini_batch_sz=250, reg=0, verbose=2):
        '''Trains the network to data in `features` belonging to the int-coded classes `y`.
        Implements stochastic mini-batch gradient descent

        Parameters:
        -----------
        features: ndarray. shape=(Num samples N, num features M)
        y: ndarray. int-coded class assignments of training samples. 0,...,numClasses-1
        n_epochs: int. Number of training epochs
        lr: float. Learning rate
        mini_batch_sz: int. Batch size per training iteration.
        reg: float. Regularization strength used when computing the loss and gradient.
        verbose: int. 0 means no print outs. Any value > 0 prints Current iteration number and
            training loss every 100 iterations.

        Returns:
        -----------
        loss_history: Python list of floats. Recorded training loss on every mini-batch / training
            iteration.
        '''

        num_samps, num_features = features.shape
        self.wts = np.random.normal(loc=0, scale=0.01, size=(num_features, self.num_output_units))
        self.b = np.random.normal(loc=0, scale=0.01, size=(1, self.num_output_units))

        loss_history = []

        if mini_batch_sz > 0:
            iterations = (num_samps // mini_batch_sz)
        else:
            iterations = 1
        
        verboseIdx = 0

        for i in range(n_epochs * iterations):
            
            # setting up mini batches
            mini_batch_indices = np.random.choice(features.shape[0],mini_batch_sz,replace=True)
            mini_batch_true_classes = y[mini_batch_indices]
            mini_batch_one_hot = self.one_hot(mini_batch_true_classes, self.num_output_units)

            if mini_batch_sz == 1:
                mini_batch = features[mini_batch_indices,:].reshape(mini_batch_sz,1)
            else:
                mini_batch = features[mini_batch_indices,:]


            # find the loss and add it to the history
            net_input = self.net_in(mini_batch)
            net_act = self.activation(net_input)
            loss = self.loss(net_act, mini_batch_true_classes, reg)
            loss_history.append(loss)

            # update the bias and the weights
            grad_wts, grad_bias = self.gradient(mini_batch, net_act, mini_batch_one_hot, reg)
            
            self.b = self.b - lr * grad_bias
            self.wts = self.wts - lr * grad_wts

            if verbose != 0 and verboseIdx % 100 == 0:
                print("Completed iter", verboseIdx, '/', n_epochs * iterations, ". Training loss: ", loss, '.')

            verboseIdx += 1

        if verbose != 0:
            print("Finished training!")
            
        return(loss_history)

    def predict(self, features):
        '''Predicts the int-coded class value for network inputs ('features').

        Parameters:
        -----------
        features: ndarray. shape=(mini-batch size, num features)

        Returns:
        -----------
        y_pred: ndarray. shape=(mini-batch size,).
        '''

        y_pred = np.argmax(self.net_in(features),axis=1)
        return y_pred

    def activation(self, net_in):
        '''Applies the softmax activation function on the net_in.

        Parameters:
        -----------
        net_in: ndarray. net in. shape=(mini-batch size, num output neurons)
        i.e. shape=(N, C)

        Returns:
        -----------
        f_z: ndarray. net_act transformed by softmax function. shape=(N, C)
        '''

        n = -np.expand_dims(np.max(net_in,axis=1),axis=1)
        f_z = np.exp(net_in+n)/np.sum(np.exp(net_in+n),axis=1, keepdims=True)

        return f_z

    def loss(self, net_act, y, reg=0):
        '''Computes the cross-entropy loss

        Parameters:
        -----------
        net_act: ndarray. softmax net activation. shape=(mini-batch size, num output neurons)
        y: ndarray. correct class values, int-coded. shape=(mini-batch size,)
        reg: float. Regularization strength

        Returns:
        -----------
        loss: float. Regularized average loss over the mini batch
        '''

        one_hot = self.one_hot(y, net_act.shape[1])
        loss = -np.sum(np.log(net_act)*one_hot)/net_act.shape[0]+1/2*np.sum(reg*self.wts**2)

        return loss

    def gradient(self, features, net_act, y, reg=0):
        '''Computes the gradient of the softmax version of the net

        Parameters:
        -----------
        features: ndarray. net inputs. shape=(mini-batch-size, Num features)
        net_act: ndarray. net outputs. shape=(mini-batch-size, C)
            In the softmax network, net_act for each input has the interpretation that
            it is a probability that the input belongs to each of the C output classes.
        y: ndarray. one-hot coded class labels. shape=(mini-batch-size, Num output neurons)
        reg: float. regularization strength.

        Returns:
        -----------
        grad_wts: ndarray. Weight gradient. shape=(Num features, C)
        grad_b: ndarray. Bias gradient. shape=(C,)
        '''

        num_inputs = len(features)

        grad_wts =(features.T @ (net_act - y))/num_inputs +reg*self.wts
        grad_b = np.sum((net_act-y),axis=0)/num_inputs
        return grad_wts, grad_b

    def test_loss(self, wts, b, features, labels):
        ''' Tester method for net_in and loss
        '''
        self.wts = wts
        self.b = b

        net_in = self.net_in(features)
        print(f'net in shape={net_in.shape}, min={net_in.min()}, max={net_in.max()}')
        print('Should be\nnet in shape=(15, 10), min=0.7160773059462714, max=1.4072103751494884\n')

        net_act = self.activation(net_in)
        print(f'net act shape={net_act.shape}, min={net_act.min()}, max={net_act.max()}')
        print('Should be\nnet act shape=(15, 10), min=0.0732240641262733, max=0.1433135816597887\n')
        return self.loss(net_act, labels, 0), self.loss(net_act, labels, 0.5)

    def test_gradient(self, wts, b, features, labels, num_unique_classes, reg=0):
        ''' Tester method for gradient
        '''
        self.wts = wts
        self.b = b

        net_in = self.net_in(features)
        print(f'net in: {net_in.shape}, {net_in.min()}, {net_in.max()}')
        print(f'net in 1st few values of 1st input are: {net_in[0, :5]}')

        net_act = self.activation(net_in)
        print(f'net act 1st few values of 1st input are: {net_act[0, :5]}')

        labels_one_hot = self.one_hot(labels, num_unique_classes)
        print(f'y one hot: {labels_one_hot.shape}, sum is {np.sum(labels_one_hot)}')

        return self.gradient(features, net_act, labels_one_hot, reg=reg)
