
interface Foo{
    int Bar;
    int X;
    double Fun();
    double Min(double a, double b);
}

void main(){
    int A;
    instance Foo B;
    let B = new Foo;
    let A = B.Fun();

    printd(A);
    printc(10);
}

storage Foo{
    int Bar = 1;
    int X = 2;
}

implementation Foo{
    double Fun(){
        return self.Min(Bar * 3.0, X * 2.0);
    }
    
    double Min(double a, double b){
        if(a < b){
            return a;
        }
        return b;
    }
}
