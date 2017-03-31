#pragma version(1)
#pragma rs java_package_name(newera.EliJ)

rs_allocation in1;
rs_allocation in2;


//MAINS
uchar4 __attribute__((kernel)) soustract(uint32_t x, uint32_t y) {
    return rsPackColorTo8888( rsUnpackColor8888(rsGetElementAt_uchar4(in1, x, y)) - rsUnpackColor8888(rsGetElementAt_uchar4(in2, x, y)) );
}