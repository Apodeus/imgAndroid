#pragma version(1)
#pragma rs java_package_name(newera.EliJ)

rs_allocation in;

int w, h;
int matrix_size = 1;
float matrix_factor = 1;
rs_allocation matrix2D;

int matrix_halfSize;
float factor;

//MAINS
uchar4 __attribute__((kernel)) convolution(uint32_t x, uint32_t y) {
    float4 stack = {0, 0, 0, 0};

    matrix_halfSize = (matrix_size-1)*0.5;

    for(int j = -matrix_halfSize; j <= matrix_halfSize; ++j){
        for(int i = -matrix_halfSize; i <= matrix_halfSize; ++i){
            factor = rsGetElementAt_float(matrix2D, (i+matrix_halfSize)+(j+matrix_halfSize)*matrix_size);
            if (x+i < w && y+j < h && x+i >= 0 && y+j >= 0){
                stack.r += rsUnpackColor8888(rsGetElementAt_uchar4(in, x+i, y+j)).r * factor;
                stack.g += rsUnpackColor8888(rsGetElementAt_uchar4(in, x+i, y+j)).g * factor;
                stack.b += rsUnpackColor8888(rsGetElementAt_uchar4(in, x+i, y+j)).b * factor;
                stack.a += rsUnpackColor8888(rsGetElementAt_uchar4(in, x+i, y+j)).a * factor;
            } else {
                stack.r += rsUnpackColor8888(rsGetElementAt_uchar4(in, x, y)).r * factor;
                stack.g += rsUnpackColor8888(rsGetElementAt_uchar4(in, x, y)).g * factor;
                stack.b += rsUnpackColor8888(rsGetElementAt_uchar4(in, x, y)).b * factor;
                stack.a += rsUnpackColor8888(rsGetElementAt_uchar4(in, x, y)).a * factor;
            }

            //stack.r += tmp_pix.r ;//* rsGetElementAt_float(matrix2D, (i+matrix_halfSize)+(j+matrix_halfSize)*matrix_dim);
            //stack.g += tmp_pix.g ;//* rsGetElementAt_float(matrix2D, (i+matrix_halfSize)+(j+matrix_halfSize)*matrix_dim);
            //stack.b += tmp_pix.b ;//* rsGetElementAt_float(matrix2D, (i+matrix_halfSize)+(j+matrix_halfSize)*matrix_dim);
            //stack.a += tmp_pix.a ;//* rsGetElementAt_float(matrix2D, (i+matrix_halfSize)+(j+matrix_halfSize)*matrix_dim);
        }
    }
    //stack = convert_uint4(rsGetElementAt_uchar4(in, x, y));

    //stack.r = stack.r / matrix_factor;
    //stack.g = stack.g / matrix_factor;
    //stack.b = stack.b / matrix_factor;
    //stack.a = stack.a / matrix_factor;

    //out = convert_uchar4(stack);

    return rsPackColorTo8888(stack/matrix_factor);
}