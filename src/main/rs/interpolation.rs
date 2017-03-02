#pragma version(1)
#pragma rs java_package_name(newera.EliJ)

float3 p0, p1, p2, p3, pixel1, pixel2;
rs_allocation orig_image;
float scale;

uchar4 __attribute__((kernel)) root(uchar4 in, uint32_t x, uint32_t y) {
    float xf = (float)x/scale;
    float yf = (float)y/scale;

    float xd = xf - ((int)xf);
    float yd = yf - ((int)yf);

    //p0 p1
    //p2 p3
        p0 = rsUnpackColor8888( rsGetElementAt_uchar4(orig_image, (int)xf, (int)yf) ).rgb;
        p1 = rsUnpackColor8888( rsGetElementAt_uchar4(orig_image, (int)xf+1, (int)yf) ).rgb;
        p2 = rsUnpackColor8888( rsGetElementAt_uchar4(orig_image, (int)xf, (int)yf+1) ).rgb;
        p3 = rsUnpackColor8888( rsGetElementAt_uchar4(orig_image, (int)xf+1, (int)yf+1) ).rgb;

        pixel1.x = p0.x*(1-xd) + p1.x*(xd);
        pixel1.y = p0.y*(1-xd) + p1.y*(xd);
        pixel1.z = p0.z*(1-xd) + p1.z*(xd);

        pixel2.x = p2.x*(1-xd) + p3.x*(xd);
        pixel2.y = p2.y*(1-xd) + p3.y*(xd);
        pixel2.z = p2.z*(1-xd) + p3.z*(xd);

        pixel1.x = pixel1.x*(1-yd) + pixel2.x*(yd);
        pixel1.y = pixel1.y*(1-yd) + pixel2.y*(yd);
        pixel1.z = pixel1.z*(1-yd) + pixel2.z*(yd);

    return rsPackColorTo8888(pixel1.r, pixel1.g, pixel1.b, 1);
    //return rsGetElementAt_uchar4(orig_image, (int)xf, (int)yf );
}