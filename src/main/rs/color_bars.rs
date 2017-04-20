#pragma version(1)
#pragma rs java_package_name(newera.EliJ)

int red;
int green;
int blue;
int pos_start;
int pos_end;
int width;


uchar4 __attribute__((kernel)) FillRed(uchar4 in, uint32_t x, uint32_t y){
    uchar4 out;
    out.a = 255;
    out.g = green;
    out.b = blue;
    float tmp = (float) x / (float) width;
    out.r = (uchar)(tmp * 255.0f);

    return out;
}


uchar4 __attribute__((kernel)) FillGreen(uchar4 in, uint32_t x, uint32_t y){
    uchar4 out;
    out.a = 255;
    out.r = red;
    out.b = blue;
    float tmp = (float) x / (float) width;
    out.g = (uchar)(tmp * 255.0f);

    return out;
}


uchar4 __attribute__((kernel)) FillBlue(uchar4 in, uint32_t x, uint32_t y){
    uchar4 out;
    out.a = 255;
    out.g = green;
    out.r = red;
    float tmp = (float) x / (float) width;
    out.b = (uchar)(tmp * 255.0f);

    return out;
}


uchar4 __attribute__((kernel)) FillAlpha(uchar4 in, uint32_t x, uint32_t y){
    uchar4 out;
    out.r = red;
    out.g = green;
    out.b = blue;
    float tmp = (float) x / (float) width;
    out.a = (uchar)(tmp * 255.0f);

    return out;
}