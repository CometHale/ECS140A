#include <stdlib.h>
#include <stdio.h>
#define nil NULL

struct global;
struct global * global_new(void);
int global_gG(struct global * self);
int global_sG(struct global * self, int __val);
char global__printc(struct global * self, char __val);
int global__printi(struct global * self, int __val);
double global__printd(struct global * self, double __val);
char global__scanc(struct global * self);
int global__scani(struct global * self);
double global__scand(struct global * self);

extern struct global global__global;
struct global *global = &global__global;
int main(int argc, char *argv[])
{
    global__printi(global, global_gG(global));
    global__printc(global, 10);
}

struct global
{
    int G;
};

struct global * global_new(void){
    struct global * self = (struct global *)malloc(sizeof(struct global));
    self->G = 2;
    return self;
}

int global_gG(struct global * self){
    return self->G;
}

int global_sG(struct global * self, int __val){
    return self->G = __val;
}

struct global global__global = {2};

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
