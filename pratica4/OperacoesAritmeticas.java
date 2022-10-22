class OperacoesAritmeticas {
    int sum(int x, int y) {
        return x + y;
    }

    int sub(int x, int y) {
        return x - y;
    }

    int mult(int x, int y) {
        return x * y;
    }

    int div(int x, int y) {
        if (y != 0)
            return x / y;
        else
            return 0;
    }
}