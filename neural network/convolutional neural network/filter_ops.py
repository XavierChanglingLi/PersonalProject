'''
Author: Changling Li
Name: filter_ops.py
Date: 10/30/20
Implements the convolution and max pooling operations.
Applied to images and other data represented as an ndarray.
'''

import numpy as np


def conv2_gray(img, kers, verbose=True):
    '''Does a 2D convolution operation on GRAYSCALE `img` using kernels `kers`.
    Uses 'same' boundary conditions.

    Parameters:
    -----------
    img: ndarray. Grayscale input image to be filtered. shape=(height img_y (px), width img_x (px))
    kers: ndarray. Convolution kernels. shape=(Num kers, ker_sz (px), ker_sz (px))
    verbose: bool.

    Returns:
    -----------
    filteredImg: ndarray. `img` filtered with all the `kers`. shape=(Num kers, img_y, img_x)
    '''
    img_y, img_x = img.shape
    n_kers, ker_x, ker_y = kers.shape

    if verbose:
        print(f'img_x={img_y}, img_y={img_x}')
        print(f'n_kers={n_kers}, ker_x={ker_x}, ker_y={ker_y}')

    if ker_x != ker_y:
        print('Kernels must be square!')
        return

    ker_sz = ker_x

    # Flip the kernels on x and y axes
    flipped_kers = np.flip(np.flip(kers, 1),2)

    # padding amount
    padding = (ker_sz-1)//2 + ((ker_sz+1) % 2)

    # create padded image
    padded_image = np.zeros(shape=(img_y+padding*2,img_x+padding*2)) # make zero array with padding on both sides
    padded_image[padding:img_y+padding, padding:img_x+padding] = img # add the original array to the center

    # final image
    filteredImg = np.ndarray(shape=(n_kers, img_y, img_x)) # create the shape

    for ker in range(n_kers):
        for y in range(img_y):
            for x in range(img_x):
                filteredImg[ker, y, x] = np.sum(padded_image[y:ker_y+y, x:ker_x+x] * flipped_kers[ker, :, :])

    return filteredImg


def conv2(img, kers, verbose=True):
    '''Does a 2D convolution operation on COLOR or grayscale `img` using kernels `kers`.
    Uses 'same' boundary conditions.

    Parameters:
    -----------
    img: ndarray. Input image to be filtered. shape=(N_CHANS, height img_y (px), width img_x (px))
        where n_chans is 1 for grayscale images and 3 for RGB color images
    kers: ndarray. Convolution kernels. shape=(Num filters, ker_sz (px), ker_sz (px))
    verbose: bool. 

    Returns:
    -----------
    filteredImg: ndarray. `img` filtered with all `kers`. shape=(Num filters, N_CHANS, img_y, img_x)
    '''

    n_chans, img_y, img_x = img.shape
    n_kers, ker_x, ker_y = kers.shape

    if verbose:
        print(f'img_x={img_y}, img_y={img_x}')
        print(f'n_kers={n_kers}, ker_x={ker_x}, ker_y={ker_y}')

    if ker_x != ker_y:
        print('Kernels must be square!')
        return

    ker_sz = ker_x

    # Flip the kernels on x and y axes
    flipped_kers = np.flip(np.flip(kers, 1),2)

    # padding amount
    padding = (ker_sz-1)//2 + ((ker_sz+1) % 2)

    # create padded image
    padded_image = np.zeros(shape=(n_chans, img_y+padding*2,img_x+padding*2)) # make zero array with padding on both sides
    padded_image[:, padding:img_y+padding, padding:img_x+padding] = img # add the original array to the center

    # final image
    filteredImg = np.ndarray(shape=(n_kers, n_chans, img_y, img_x)) # create the shape

    for ker in range(n_kers):
        for y in range(img_y):
            for x in range(img_x):
                filteredImg[ker, :, y, x] = np.sum(padded_image[:, y:ker_y+y, x:ker_x+x] * flipped_kers[ker, :, :], axis=(1,2))

    return filteredImg


def conv2nn(imgs, kers, bias, verbose=True):
    '''General 2D convolution operation suitable for a convolutional layer of a neural network.
    Uses 'same' boundary conditions.

    Parameters:
    -----------
    imgs: ndarray. Input IMAGES to be filtered. shape=(BATCH_SZ, n_chans, img_y, img_x)
        where batch_sz is the number of images in the mini-batch
        n_chans is 1 for grayscale images and 3 for RGB color images
    kers: ndarray. Convolution kernels. shape=(n_kers, N_CHANS, ker_sz, ker_sz)
    bias: ndarray. Bias term used in the neural network layer. Shape=(n_kers,)
    verbose: bool. 

    Returns:
    -----------
    output: ndarray. `imgs` filtered with all `kers`. shape=(BATCH_SZ, n_kers, img_y, img_x)
    '''
    batch_sz, n_chans, img_y, img_x = imgs.shape
    n_kers, n_ker_chans, ker_x, ker_y = kers.shape

    if verbose:
        print(f'img_x={img_y}, img_y={img_x}')
        print(f'n_kers={n_kers}, ker_x={ker_x}, ker_y={ker_y}')

    if ker_x != ker_y:
        print('Kernels must be square!')
        return

    ker_sz = ker_x

    # Flip the kernels on x and y axes
    flipped_kers = np.flip(np.flip(kers, 2),3)

    # padding amount
    padding = (ker_sz-1)//2 + ((ker_sz+1) % 2)

    # create padded image
    padded_image = np.zeros(shape=(batch_sz, n_chans, img_y+padding*2,img_x+padding*2)) # make zero array with padding on both sides
    padded_image[:, :, padding:img_y+padding, padding:img_x+padding] = imgs # add the original array to the center

    # final image
    filteredImg = np.ndarray(shape=(batch_sz, n_kers, img_y, img_x)) # create the shape
    
    for ker in range(n_kers):
        for y in range(img_y):
            for x in range(img_x):
                filteredImg[:, ker, y, x] = np.sum(padded_image[:, :, y:ker_y+y, x:ker_x+x] * flipped_kers[ker, :, :, :] , axis=(1,2,3)) + bias[ker]

    return filteredImg


def get_pooling_out_shape(img_dim, pool_size, strides):
    '''Computes the size of the output of a max pooling operation along one spatial dimension.

    Parameters:
    -----------
    img_dim: int. Either img_y or img_x
    pool_size: int. Size of pooling window in one dimension: either x or y (assumed the same).
    strides: int. Size of stride when the max pooling window moves from one position to another.

    Returns:
    -----------
    int. The size in pixels of the output of the image after max pooling is applied, in the dimension
        img_dim.
    '''
    size_img = int((img_dim - pool_size)/strides+1)
    return size_img


def max_pool(inputs, pool_size=2, strides=1, verbose=True):
    ''' Does max pooling on inputs. Works on single grayscale images, so somewhat comparable to
    `conv2_gray`.

    Parameters:
    -----------
    inputs: Input to be filtered. shape=(height img_y, width img_x)
    pool_size: int. Pooling window extent in both x and y.
    strides: int. How many "pixels" in x and y to skip over between successive max pooling operations
    verbose: bool. 

    Returns:
    -----------
    outputs: Input filtered with max pooling op. shape=(out_y, out_x)
    '''
    img_y, img_x = inputs.shape
    pool_img_x = get_pooling_out_shape(img_x, pool_size, strides)
    pool_img_y = get_pooling_out_shape(img_y, pool_size, strides)
    pool_img = np.zeros((pool_img_y,pool_img_x))
    # print(pool_img.shape)
    for y in range(pool_img_y):
        for x in range(pool_img_x):
            # print(pool_img)
            pool_img[y,x] = np.amax(inputs[strides*y:strides*y+pool_size,strides*x:strides*x+pool_size])
    return pool_img


def max_poolnn(inputs, pool_size=2, strides=1, verbose=True):
    ''' Max pooling implementation for a MaxPooling2D layer of a neural network

    Parameters:
    -----------
    inputs: Input to be filtered. shape=(mini_batch_sz, n_chans, height img_y, width img_x)
        where n_chans is 1 for grayscale images and 3 for RGB color images
    pool_size: int. Pooling window extent in both x and y.
    strides: int. How many "pixels" in x and y to skip over between successive max pooling operations
    verbose: bool. 
    Returns:
    -----------
    outputs: Input filtered with max pooling op. shape=(mini_batch_sz, n_chans, out_y, out_x)
    '''
    mini_batch_sz, n_chans, img_y, img_x = inputs.shape

    if verbose:
        print(f'img_x={img_x}, img_y={img_y}, pool_size={pool_size}, strides={strides}')
    pool_img_x = get_pooling_out_shape(img_x, pool_size, strides)
    pool_img_y = get_pooling_out_shape(img_y, pool_size, strides)
    pool_img = np.zeros((mini_batch_sz,n_chans,pool_img_y,pool_img_x))
    # print(pool_img.shape)
    for batch in range(mini_batch_sz):
        for chan in range(n_chans):
            for y in range(pool_img_y):
                for x in range(pool_img_x):
            # print(pool_img)
                    pool_img[batch, chan, y,x] = np.amax(inputs[batch, chan, strides*y:strides*y+pool_size,strides*x:strides*x+pool_size])
    return pool_img

