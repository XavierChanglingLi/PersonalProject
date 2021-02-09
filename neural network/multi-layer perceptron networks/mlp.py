'''
Author: Changling Li
Name: mlp.py
Date: 10/15/20
Constructs, trains, tests 3 layer multilayer layer perceptron networks
'''

import numpy as np


class MLP():
    '''
    The structure of MLP:
    Input layer (X units) ->
    Hidden layer (Y units) with Rectified Linear activation (ReLu) ->
    Output layer (Z units) with softmax activation
    '''

    def __init__(self, num_input_units, num_hidden_units, num_output_units):
        '''
        Parameters:
        -----------
        num_input_units: int. Num input features
        num_hidden_units: int. Num hidden units
        num_output_units: int. Num output units. Equal to # data classes.
        '''
        self.num_input_units = num_input_units
        self.num_hidden_units = num_hidden_units
        self.num_output_units = num_output_units
        self.y_wts = None
        self.y_b = None
        self.z_wts = None
        self.z_b = None
        self.initialize_wts(num_input_units, num_hidden_units, num_output_units)

    def get_y_wts(self):
        '''Returns a copy of the hidden layer wts'''
        return self.y_wts.copy()

    def initialize_wts(self, M, H, C, std=0.1):
        ''' Randomly initialize the hidden and output layer weights and bias term
        Parameters:
        -----------
        M: int. Num input features
        H: int. Num hidden units
        C: int. Num output units. Equal to # data classes.
        std: float. Standard deviation of the normal distribution of weights

        Returns:
        -----------
        No return
        '''

        # keep the random seed for debugging/test code purposes
        np.random.seed(0)
        self.y_wts = np.random.normal(loc=0, scale=std, size=(M,H))
        self.y_b = np.random.normal(loc=0, scale=std, size=(H,))
        self.z_wts = np.random.normal(loc=0, scale=std, size=(H, C))
        self.z_b = np.random.normal(loc=0, scale=std, size=(C,))

    def accuracy(self, y, y_pred):
        ''' Computes the accuracy of classified samples. Proportion correct
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

    def one_hot(self, y, num_classes):
        '''One-hot codes the output classes for a mini-batch
        Parameters:
        -----------
        y: ndarray. int-coded class assignments of training mini-batch. 0,...,numClasses-1
        num_classes: int. Number of unique output classes total

        Returns:
        -----------
        y_one_hot: One-hot coded class assignments.
        '''

        y_one_hot = np.repeat(y.reshape(y.shape[0],1), num_classes, axis=1)
        for i in range(num_classes):
            y_one_hot[:,i]=np.where(y_one_hot[:,i]==i,1,0)
        return y_one_hot

    def predict(self, features):
        ''' Predicts the int-coded class value for network inputs ('features').
        Parameters:
        -----------
        features: ndarray. shape=(mini-batch size, num features)

        Returns:
        -----------
        y_pred: ndarray. shape=(mini-batch size,).
            This is the int-coded predicted class values for the inputs passed in.
            Note: You can figure out the predicted class assignments without applying the
            softmax net activation function — it will not affect the most active neuron.
        '''

        y_net_in = features@self.y_wts + self.y_b
        y_net_act = y_net_in
        y_net_act[y_net_in<0]=0
        net_input = y_net_act@self.z_wts +self.z_b
        y_pred = np.argmax(net_input,axis=1)
        return y_pred

    def forward(self, features, y, reg=0):
        '''
        Performs a forward pass of the net (input -> hidden -> output).

        Parameters:
        -----------
        features: ndarray. net inputs. shape=(mini-batch-size N, Num features M)
        y: ndarray. int coded class labels. shape=(mini-batch-size N,)
        reg: float. regularization strength.

        Returns:
        -----------
        y_net_in: ndarray. shape=(N, H). hidden layer "net in"
        y_net_act: ndarray. shape=(N, H). hidden layer activation
        z_net_in: ndarray. shape=(N, C). output layer "net in"
        z_net_act: ndarray. shape=(N, C). output layer activation
        loss: float. REGULARIZED loss derived from output layer, averaged over all input samples
        '''

        #ReLu for the hidden layer netAct
        y_net_in = features@self.y_wts + self.y_b
        y_net_act = y_net_in
        y_net_act[y_net_in<0]=0
        #softmax for out layer netAct
        z_net_in = y_net_act@self.z_wts+self.z_b
        n = -np.expand_dims(np.max(z_net_in,axis=1),axis=1)
        z_net_act = np.exp(z_net_in+n)/np.sum(np.exp(z_net_in+n),axis=1,keepdims=True)

        one_hot = self.one_hot(y, z_net_act.shape[1])
        loss = -np.sum(np.log(z_net_act)*one_hot)/z_net_act.shape[0]+1/2*reg*np.sum(self.z_wts**2)+1/2*reg*np.sum(self.y_wts**2)
        return y_net_in, y_net_act, z_net_in, z_net_act, loss

    def backward(self, features, y, y_net_in, y_net_act, z_net_in, z_net_act, reg=0):
        '''
        Performs a backward pass (output -> hidden -> input) 

        Parameters:
        -----------
        features: ndarray. net inputs. shape=(mini-batch-size, Num features)
        y: ndarray. int coded class labels. shape=(mini-batch-size,)
        y_net_in: ndarray. shape=(N, H). hidden layer "net in"
        y_net_act: ndarray. shape=(N, H). hidden layer activation
        z_net_in: ndarray. shape=(N, C). output layer "net in"
        z_net_act: ndarray. shape=(N, C). output layer activation
        reg: float. regularization strength.

        Returns:
        -----------
        dy_wts, dy_b, dz_wts, dz_b: The following backwards gradients
        (1) hidden wts, (2) hidden bias, (3) output weights, (4) output bias
        '''

        one_hot = self.one_hot(y, z_net_act.shape[1])
        # 5 loss WRT netAct
        dz_net_act = -1/(len(z_net_act) * z_net_act)
        # 4 netAct to netIn
        dz_net_in = dz_net_act*(z_net_act*(one_hot-z_net_act))
        # step 3
        dz_wts = (dz_net_in.T@y_net_act).T+reg*self.z_wts
        dz_b = np.sum(dz_net_in, axis=0)
        # step 3 to 2
        dy_net_act = (dz_net_in@self.z_wts.T)
        # step 2
        dy_net_in = (dy_net_act)*np.where(y_net_in>0,1,0)

        dy_wts = (dy_net_in.T@features).T+reg*self.y_wts
        dy_b = np.sum(dy_net_in,axis=0)

        return dy_wts, dy_b, dz_wts, dz_b

    def fit(self, features, y, x_validation, y_validation,
            resume_training=False, n_epochs=500, lr=0.0001, mini_batch_sz=256, reg=0, verbose=2,
            print_every=100):
        ''' Trains the network to data in `features` belonging to the int-coded classes `y`.
        Implements stochastic mini-batch gradient descent

        Parameters:
        -----------
        features: ndarray. shape=(Num samples N, num features).
            Features over N inputs.
        y: ndarray.
            int-coded class assignments of training samples. 0,...,numClasses-1
        x_validation: ndarray. shape=(Num samples in validation set, num features).
        y_validation: ndarray.
            int-coded class assignments of validation samples. 0,...,numClasses-1
        resume_training: bool.
            False: we clear the network weights and do fresh training
            True: we continue training based on the previous state of the network.
                This is handy if runs of training get interupted and you'd like to continue later.
        n_epochs: int.
            Number of training epochs
        lr: float.
            Learning rate
        mini_batch_sz: int.
            Batch size per epoch.
        reg: float.
            Regularization strength used when computing the loss and gradient.
        verbose: int.
            0 means no print outs. Any value > 0 prints Current epoch number and training loss every
            `print_every` (e.g. 100) epochs.
        print_every: int.
            If verbose > 0, print out the training loss and validation accuracy over the last epoch
            every `print_every` epochs.

        Returns:
        -----------
        loss_history: Python list of floats.
            Recorded training loss on every epoch for the current mini-batch.
        train_acc_history: Python list of floats.
            Recorded accuracy on every training epoch on the current training mini-batch.
        validation_acc_history: Python list of floats.
            Recorded accuracy on every epoch on the validation set.

        '''
        num_samps, num_features = features.shape
        num_classes = self.num_output_units

        loss_history = []
        train_acc_history = []
        validation_acc_history = []

        if mini_batch_sz>0:
            iterations = (num_samps//mini_batch_sz)
        else:
            iterations = 1

        verboseIdx = 0

        for i in range(n_epochs*iterations):

            #setting up mini batch
            mini_batch_indices = np.random.choice(num_samps, mini_batch_sz, replace=True)
            mini_batch_true_class = y[mini_batch_indices].reshape(mini_batch_sz,1)
            mini_batch = features[mini_batch_indices,:]

            #forward prop
            y_net_in, y_net_act, z_net_in, z_net_act, loss = self.forward(mini_batch, mini_batch_true_class,reg)
            loss_history.append(loss)
            #backprop
            dy_wts, dy_b, dz_wts, dz_b = self.backward(mini_batch, mini_batch_true_class, y_net_in, y_net_act, z_net_in, z_net_act, reg)
            #updata weights
            self.y_wts = self.y_wts-lr*dy_wts
            self.y_b = self.y_b - lr*dy_b

            self.z_wts = self.z_wts - lr*dz_wts
            self.z_b = self.z_b - lr*dz_b
            #get prediction
            if i%iterations==0:
                train_acc = self.accuracy(y, self.predict(features))
                train_acc_history.append(train_acc)

                validation_acc = self.accuracy(y_validation, self.predict(x_validation))
                validation_acc_history.append(validation_acc)
                
            if verbose != 0 and verboseIdx % (iterations*print_every)==0:
                print("Completed Epoch", int(verboseIdx/iterations), '/', n_epochs, ". Training loss: ", loss, ". Validation accuracy:", validation_acc)
            verboseIdx +=1
        print("Finished training!")

        return loss_history, train_acc_history, validation_acc_history

