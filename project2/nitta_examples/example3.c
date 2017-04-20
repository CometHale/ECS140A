#include <stdlib.h>
#include <stdio.h>
#define nil NULL

struct global;
struct global * global_new(void);
char global__printc(struct global * self, char __val);
int global__printi(struct global * self, int __val);
double global__printd(struct global * self, double __val);
char global__scanc(struct global * self);
int global__scani(struct global * self);
double global__scand(struct global * self);

struct Foo;
struct Foo * Foo_new(void);
int Foo_gBar(struct Foo * self);
int Foo_sBar(struct Foo * self, int __val);
int Foo_gX(struct Foo * self);
int Foo_sX(struct Foo * self, int __val);
double Foo__Fun(struct Foo * self);
double Foo__Min(struct Foo * self, double __a, double __b);

extern struct global global__global;
struct global *global = &global__global;
int main(int argc, char *argv[])
{
    int __A;
    struct Foo * __B;
    __B = Foo_new();
    __A = Foo__Fun(__B);
    global__printd(global, __A);
    global__printc(global, 10);
}

struct Foo
{
    int Bar;
    int X;
};

struct Foo * Foo_new(void){
    struct Foo * self = (struct Foo *)malloc(sizeof(struct Foo));
    self->Bar = 1;
    self->X = 2;
    return self;
}

int Foo_gBar(struct Foo * self){
    return self->Bar;
}

int Foo_sBar(struct Foo * self, int __val){
    return self->Bar = __val;
}

int Foo_gX(struct Foo * self){
    return self->X;
}

int Foo_sX(struct Foo * self, int __val){
    return self->X = __val;
}

struct global
{
};

struct global * global_new(void){
    struct global * self = (struct global *)malloc(sizeof(struct global));
    return self;
}

struct global global__global;

char global__printc(struct global *self, char val){
    printf("%c",val);
    return val;
}

int global__printi(struct global *self, int val){
    printf("%d",val);
    return val;
}

double global__printd(struct global *self, double val){
    printf("%lf",val);
    return val;
}

char global__scanc(struct global *self){
    char RetVal;
    scanf("%c",&RetVal);
    return RetVal;
}

int global__scani(struct global *self){
    int RetVal;
    scanf("%d",&RetVal);
    return RetVal;
}

double global__scand(struct global *self){
    double RetVal;
    scanf("%lf",&RetVal);
    return RetVal;
}

double Foo__Fun(struct Foo * self)
{
    return Foo__Min(self, self->Bar * 3.0, self->X * 2.0);
}

double Foo__Min(struct Foo * self, double __a, double __b)
{
    if(__a < __b){
        return __a;
    }
    return __b;
}
