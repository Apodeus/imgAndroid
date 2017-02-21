#pragma version(1)
#pragma rs java_package_name(newera.myapplication)

#include "utility.rsh"

float newHue; // between 0 and 6
float epsilon;

static int3 keepHue(float3 pixel, float hue){
    float3 hsl = rgbToHsl(pixel.r, pixel.g, pixel.b);
    float3 new = hslToRGB(hsl.x, hsl.y, hsl.z);

    float borneInf = newHue - epsilon;
    float borneSup = newHue + epsilon;

    if(borneInf < 0){
        borneSup = 6.0f - fabs(borneInf);
        borneInf = newHue + epsilon;
    }
    if(borneSup > 6.0f){
        borneInf = fabs(fmod(borneSup, 6.0f));
        borneSup = newHue - epsilon;
    }

    if(!(hsl.x >= borneInf && hsl.x <= borneSup)){//if the pixel's hue is not in the range [newHue-epsilon; newHue+epsilon], we convert it as gray
        int r, g, b;
        r = (int)(new.x * 255.0f);
        g = (int)(new.y * 255.0f);
        b = (int)(new.z * 255.0f);
        int3 pixelGS = convertGrayScale(r, g, b);
        return pixelGS;
    }
    int3 newPixel = {(int)(new.x * 255.0f), (int)(new.y * 255.0f), (int)(new.z * 255.0f)};
    //new.x = new.x * 255.0f;
    //new.y = new.y * 255.0f;
    //new.z = new.z * 255.0f;
    return newPixel;

}




//MAINS
uchar4 __attribute__((kernel)) KeepSpecificHue(uchar4 in, uint32_t x, uint32_t y) {
    float3 rgb = {in.r, in.g, in.b};

    int3 new = keepHue(rgb, newHue);
    uchar4 out;

    out.a = in.a;
    out.r = (uchar)(new.x);
    out.g = (uchar)(new.y);
    out.b = (uchar)(new.z);

    return out;
}