#pragma version(1)
#pragma rs java_package_name(newera.EliJ)

rs_allocation src;
rs_allocation cut;

uchar4 __attribute__((kernel)) Fuse(uchar4 in, uint32_t x, uint32_t y) {
    uchar4 out;
    uchar4 ssrc = rsGetElementAt_uchar4(src, x, y);
    out.a = 255;
    out.r = in.r * in.a/255 + ssrc.r * (255 - in.a)/255;
    out.g = in.g * in.a/255 + ssrc.g * (255 - in.a)/255;
    out.b = in.b * in.a/255 + ssrc.b * (255 - in.a)/255;
    //out = in*in.a + ssrc*(255-in.a);
    return out;
}

uchar4 __attribute__((kernel)) Cut(uchar4 in, uint32_t x, uint32_t y) {
    uchar4 out;
    if (in.a > 0)
    {
        uchar4 scut = rsGetElementAt_uchar4(cut, x, y);
        out = scut;
    }else{
        uchar4 ssrc = rsGetElementAt_uchar4(src, x, y);
        out = ssrc;
    }
    return out;
}

