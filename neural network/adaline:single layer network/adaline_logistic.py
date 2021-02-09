'''
Author: Changling Li
Name: adaline_logistic.py
Date: 9/30/20
ADALINE logistic neural network for classification and regression
'''

import numpy as np
import math
from adaline import Adaline

class AdalineLogistic(Adaline):
    def __init__(self):
        Adaline.__init__(self)

    def activation(self, net_in):
        '''
        Parameters:
        ----------
        net_in: ndarray. Shape = [Num samples N,]

        Returns:
        ----------
        net_act. ndarray. Shape = [Num samples N,]
        '''

        net_act = 1/(1+math.e**(-net_in))
        return net_act

    def predict(self, features):
        '''Predicts the class of each test input sample

        Parameters:
        ----------
        features: ndarray. Shape = [Num samples N, Num features M]
            Collection of input vectors.

        Returns:
        ----------
        The predicted classes (-1 or +1) for each input feature vector. Shape = [Num samples N,]
        '''

        net_in = self.net_input(features)
        net_act = self.activation(net_in)

        prediction = np.where(net_act >= 0.5, 1, 0)
        return prediction

    def compute_loss(self, y, net_act):
        ''' Computes the Sum of Squared Error (SSE) loss (over a single training epoch)

        Parameters:
        ----------
        y: ndarray. Shape = [Num samples N,]
            True classes corresponding to each input sample in a training epoch (coded as -1 or +1).
        net_act: ndarray. Shape = [Num samples N,]
            Output neuron's activation value (after activation function is applied)

        Returns:
        ----------
        float. The SSE loss (across a single training epoch).
        '''
        
        loss = -y@np.log(net_act)-(1-y)@np.log(1-net_act)
        return loss
