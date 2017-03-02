#pragma version(1)
#pragma rs java_package_name(newera.EliJ)

rs_allocation in_h;
rs_allocation in_v;

//MAINS
uchar4 __attribute__((kernel)) sobel(uint32_t x, uint32_t y) {
    float4 h = rsUnpackColor8888(rsGetElementAt_uchar4(in_h, x, y));
    float4 v = rsUnpackColor8888(rsGetElementAt_uchar4(in_v, x, y));

    return rsPackColorTo8888( sqrt(pow(h, 2)- pow(v, 2)) );
}