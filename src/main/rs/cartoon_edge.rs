#pragma version(1)
#pragma rs java_package_name(newera.EliJ)

rs_allocation in;

int w, h;
int matrix_size = 1;
float matrix_factor = 1;
rs_allocation matrix2D;
int mx, my;

int matrix_halfSize;
float factor;

//MAINS
uchar4 __attribute__((kernel)) cartoon_edge(uint32_t x, uint32_t y) {
    float4 stack = {0, 0, 0, 0};

    matrix_halfSize = (matrix_size-1)*0.5;

    for(int j = -matrix_halfSize; j <= matrix_halfSize; ++j){
        for(int i = -matrix_halfSize; i <= matrix_halfSize; ++i){
            factor = rsGetElementAt_float(matrix2D, (i+matrix_halfSize)+(j+matrix_halfSize)*matrix_size);
            mx = x+i;
            my = y+j;
            if(mx>=w)mx=w-1;
            else if(mx<0)mx=0;
            if(my>=h)my=h-1;
            else if(my<0)my=0;
            stack += rsUnpackColor8888(rsGetElementAt_uchar4(in, mx, my)) * factor;
        }
    }
    if (stack.r+stack.g+stack.b < 0.5) stack = 0;

    return rsPackColorTo8888(stack*2);
}