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
char global__Foo(struct global * self);

extern struct global global__global;
struct global *global = &global__global;
int main(int argc, char *argv[])
{
    int __A;
    __A = global__Foo(global);
    global__printc(global, __A);
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

char global__Foo(struct global * self)
{
    return 10;
}
