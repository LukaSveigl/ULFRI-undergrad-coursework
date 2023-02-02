import numpy as np

def spiral_ccw(A):
    A = np.array(A)
    out = []
    print(A,"\n")
    while(A.size):
        out.append(A[0][::-1])    # first row reversed
        A = A[1:][::-1].T         # cut off first row and rotate clockwise
    return np.concatenate(out)


def base_spiral(nrow, ncol):
    return spiral_ccw(np.arange(nrow*ncol).reshape(nrow,ncol))[::-1]

def to_spiral(A):
    A = np.array(A)
    B = np.empty_like(A)
    B.flat[base_spiral(*A.shape)] = A.flat
    return B

N = int(input("Enter N: "))
arr = np.flip(np.arange(N ** 2).reshape(N,N)) + 1

print(to_spiral(arr))