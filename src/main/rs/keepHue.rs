#pragma version(1)
#pragma rs java_package_name(newera.myapplication)

#include "rs_debug.rsh"
#include "utility.rsh"

float newHue; // between [0;2.0[
float epsilon;

static int3 keepHue(float3 pixel, float hue){
    float3 hsl = rgbToHsl(pixel.x, pixel.y, pixel.z);
    float3 new = hslToRGB(hsl.x, hsl.y, hsl.z);

    //newHue = restreinHue(newHue);

    float borneInf = newHue - epsilon;
    float borneSup = newHue + epsilon;

    if(borneInf < 0){
        //borneInf = 2.0f - fabs(fmod(borneInf, 2.0f));
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
            r = (int)(new.x * 255.0f);
            g = (int)(new.y * 255.0f);
            b = (int)(new.z * 255.0f);
            int3 pixelGS = convertGrayScale(r, g, b);
            return pixelGS;
        } else {
            int3 newPixel = {(int)(new.x * 255.0f), (int)(new.y * 255.0f), (int)(new.z * 255.0f)};
            //new.x = new.x * 255.0f;
            //new.y = new.y * 255.0f;
            //new.z = new.z * 255.0f;
            return newPixel;
        }
    } else {

        //case 2
        //if the pixel's hue is not in the range [newHue-epsilon; newHue+epsilon], we convert it as gray
        if(!( hsl.x >= borneInf && hsl.x <= borneSup) ){
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
}

//MAINS
uchar4 __attribute__((kernel)) KeepSpecificHue(uchar4 in, uint32_t x, uint32_t y) {
    float3 rgb = {in.r, in.g, in.b};

    int3 new = keepHue(rgb, newHue);
    //float3 newt = rgbToHsl(rgb.r, rgb.g, rgb.b);
    //float3 new = hslToRGB(newt.x, newt.y, newt.z);
    uchar4 out;

    out.a = in.a;
    out.r = (uchar)(new.x);
    out.g = (uchar)(new.y);
    out.b = (uchar)(new.z);

    return out;
}