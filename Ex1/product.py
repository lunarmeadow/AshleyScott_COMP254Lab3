import sys

def product(m, n):
    # use m as a counter...
    # this is our base case that will halt when finished,
    # or ignore multiplications by zero
    if m == 0:
        return 0

    # decrement M until zero, adding N until then.
    # when the recursive function returns, M will be exhausted
    # and the return value will be n*m.
    return product(m - 1, n) + n

def main():
    # print 5 test cases.
    # worth noting that max stack depth by default on python is 1k calls
    print(f'\nmax stack depth (don\'t let m exceed this!) = {sys.getrecursionlimit()}\n')
    print(f'product(4, 5) = {product(4, 5)}')
    print(f'product(10, 50) = {product(10, 50)}')
    print(f'product(8, 64) = {product(8, 64)}')
    print(f'product(512, 512) = {product(512, 512)}')
    print(f'product(997, 10000) = {product(997, 10000)}\n')

if __name__ == "__main__":
    main()