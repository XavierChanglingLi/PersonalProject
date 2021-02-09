'''
Author: Changling Li
Name: optimizer.py
Date: 10/30/20
Algorithms to optimize the weights during gradient descent / backprop
'''

import numpy as np
np.random.seed(0)


class Optimizer():
    def __init__(self):
        self.wts = None
        self.d_wts = None

    def prepare(self, wts, d_wts):
        '''Stores weights and their gradient before an update step is performed.
        '''
        self.wts = wts
        self.d_wts = d_wts

    def update_weights(self):
        pass

    @staticmethod
    def create_optimizer(name, **kwargs):
        '''
        Factory method that takes in a string, and returns a new object of the
        desired type. 
        '''
        if name.lower() == 'sgd':
            return SGD(**kwargs)
        elif name.lower() == 'sgd_momentum':
            return SGD_Momentum(**kwargs)
        elif name.lower() == 'adam':
            return Adam(**kwargs)
        else:
            raise ValueError('Unknown optimizer name!')


class SGD(Optimizer):
    '''Update weights using Stochastic Gradient Descent (SGD) update rule.
    '''
    def __init__(self, lr=0.1):
        '''
        Parameters:
        -----------
        lr: float > 0. Learning rate.
        '''
        self.lr = lr

    def update_weights(self):
        '''Updates the weights according to SGD and returns a deep COPY of the
        updated weights for this time step.

        Returns:
        -----------
        A COPY of the updated weights for this time step.

        '''
        self.wts = self.wts - self.lr*self.d_wts
        return self.wts.copy()


class SGD_Momentum(Optimizer):
    '''Update weights using Stochastic Gradient Descent (SGD) with momentum
    update rule.
    '''
    def __init__(self, lr=0.001, m=0.9):
        '''
        Parameters:
        -----------
        lr: float > 0. Learning rate.
        m: float 0 < m < 1. Amount of momentum from gradient on last time step.
        '''
        self.lr = lr
        self.m = m
        self.velocity = None

    def update_weights(self):
        '''Updates the weights according to SGD with momentum and returns a
        deep COPY of the updated weights for this time step.

        Returns:
        -----------
        A COPY of the updated weights for this time step.
        '''
        if self.velocity is None:
            self.velocity = 0
        self.velocity = self.m*self.velocity - self.lr*self.d_wts
        self.wts = self.wts + self.velocity
        return self.wts.copy()


class Adam(Optimizer):
    '''Update weights using the Adam update rule.
    '''
    def __init__(self, lr=0.001, beta1=0.9, beta2=0.999, eps=1e-8, t=0):
        '''
        Parameters:
        -----------
        lr: float > 0. Learning rate.
        beta1: float 0 < m < 1. Amount of momentum from gradient on last time step.
        beta2: float 0 < m < 1. Amount of momentum from gradient on last time step.
        eps: float. Small number to prevent division by 0.
        t: int. Records the current time step: 0, 1, 2, ....
        '''
        self.lr = lr
        self.beta1 = beta1
        self.beta2 = beta2
        self.eps = eps
        self.t = t

        self.m = None
        self.v = None

    def update_weights(self):
        '''Updates the weights according to Adam and returns a
        deep COPY of the updated weights for this time step.

        Returns:
        -----------
        A COPY of the updated weights for this time step.

        '''
        if self.m is None:  
            self.m = 0
        if self.v is None:
            self.v = 0
        self.t +=1
        self.m = self.beta1*self.m + (1-self.beta1)*self.d_wts
        self.v = self.beta2*self.v + (1-self.beta2)*self.d_wts**2
        n = self.m/(1-self.beta1**self.t)
        u = self.v/(1-self.beta2**self.t)
        self.wts = self.wts - (self.lr*n)/(u**(1/2)+self.eps)
        return self.wts.copy()
