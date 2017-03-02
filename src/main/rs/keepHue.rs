#pragma version(1)
#pragma rs java_package_name(newera.myapplication)

#include "rs_debug.rsh"
#include "utility.rsh"

float newHue;
float epsilon;

static int3 keepHue(float3 pixel, float hue){
    float3 hsl = RgbToHsl(pixel);
    float3 new = HslToRgb(hsl);
    hsl.x = restreinHue(hsl.x) / 360.0f;


    newHue = restreinHue(newHue * 360.0f) / 360.0f;

    float borneInf = newHue - epsilon;
    float borneSup = newHue + epsilon;

    if(borneInf < 0){
        borneInf = 1.0f + fmod(borneInf, 1.0f);
    }

    borneSup =  fmod(newHue + epsilon, 1.0f);

    if(epsilon >= 0.5f){
        borneInf = 0;
        borneSup = 1.0f;
    }

    //case 1
    if(borneInf > borneSup){
        if((hsl.x > borneSup && hsl.x < borneInf)){
            int r, g, b;
            r = (int)(new.x);
            g = (int)(new.y);
            b = (int)(new.z);
            int3 pixelGS = convertGrayScale(r, g, b);
            return pixelGS;
        } else {
            int3 newPixel = {(int)(new.x), (int)(new.y), (int)(new.z)};
            return newPixel;
        }
    } else {

        //case 2
        //if the pixel's hue is not in the range [newHue-epsilon; newHue+epsilon], we convert it as gray
        if(!( hsl.x >= borneInf && hsl.x <= borneSup) ){
            int r, g, b;
            r = (int)(new.x);
            g = (int)(new.y);
            b = (int)(new.z);
            int3 pixelGS = convertGrayScale(r, g, b);
            return pixelGS;
        }

        int3 newPixel = {(int)(new.x), (int)(new.y), (int)(new.z)};
        return newPixel;
    }
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