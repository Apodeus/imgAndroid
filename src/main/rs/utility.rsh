#pragma version(1)
#pragma rs java_package_name(newera.EliJ)

const static float3 grayscaleValues = {0.299f, 0.587f, 0.114f};

static float Hue_2_RGB(float v1, float v2, float vHue) {
    if (vHue < 0) {
        vHue += 1;
    }

    if (vHue > 1) {
        vHue -= 1;
    }

    if ((6 * vHue) < 1) {
        return (v1 + (v2 - v1) * 6 * vHue);
    }

    if ((2 * vHue) < 1) {
        return v2;
    }

    if ((3 * vHue) < 2) {
        return (v1 + (v2 - v1) * ((2.0f / 3.0f) - vHue) * 6);
    }

    return v1;
}

static float3 HslToRgb(float3 hsl) {
	float3 rgb;
    if (hsl.y == 0) {
        // gray values
        rgb.r = hsl.z;
        rgb.g = hsl.z;
        rgb.b = hsl.z;
    } else {
        float v1, v2;
        float hue = hsl.x / 360.0f;

        v2 = (hsl.z < 0.5) ? (hsl.z * (1 + hsl.y)) : ((hsl.z + hsl.y) - (hsl.z * hsl.y));
        v1 = 2 * hsl.z - v2;

        rgb.r = Hue_2_RGB(v1, v2, hue + (1.0f / 3));
        rgb.g = Hue_2_RGB(v1, v2, hue);
        rgb.b = Hue_2_RGB(v1, v2, hue - (1.0f / 3));
    }
    if(rgb.z >= 255.0) rgb.z = 255.0;
        else if (rgb.z <= 0.0) rgb.z = 0.0;
    if(rgb.y >= 255.0) rgb.y = 255.0;
               else if (rgb.y <= 0.0) rgb.y = 0.0;
    if(rgb.x >= 255.0) rgb.x = 255.0;
             else if (rgb.x <= 0.0) rgb.x = 0.0;
    return rgb;
}

static float restreinHue(float h){
    float newHue = h;
    if (newHue <= 0){
        newHue = 360.0f - fabs(fmod(newHue, 360.0f));
    }
    if(newHue >= 360.0f){
        newHue = 0 + fabs(fmod(newHue, 360.0f));
    }
    return newHue;
}

static int3 convertGrayScale(int r, int g, int b){
      int grayscale = r * grayscaleValues.r +  g * grayscaleValues.g + b * grayscaleValues.b;
      int3 new;
      new.x = grayscale;
      new.y = grayscale;
      new.z = grayscale;
      return new;
}

static float3 RgbToHsl(float3 rgb) {
	float3 hsl;

    float valMin = fmin(fmin(rgb.r, rgb.g), rgb.b);
    float valMax = fmax(fmax(rgb.r, rgb.g), rgb.b);
    float delta = valMax - valMin;

    hsl.z = (valMax + valMin) / 2;

    if(hsl.z >= 255.0) hsl.z = 255.0;
    else if (hsl.z <= 0.0) hsl.z = 0.0;


    if (delta == 0) {
        hsl.x = 0;
        hsl.y = 0;
    } else {
        hsl.y = (hsl.z < 0.5f) ? (delta / (valMax + valMin)) : (delta / (2.0f - valMax - valMin));

        float del_r = (((valMax - rgb.r) / 6) + (delta / 2)) / delta;
        float del_g = (((valMax - rgb.g) / 6) + (delta / 2)) / delta;
        float del_b = (((valMax - rgb.b) / 6) + (delta / 2)) / delta;
        float hue;

        if (rgb.r == valMax) {
            hue = del_b - del_g;
        } else if (rgb.g == valMax) {
            hue = (1.0f / 3) + del_r - del_b;
        } else {
            hue = (2.0f / 3) + del_g - del_r;
        }

        if (hue < 0) {
            hue += 1;
        }
        if (hue > 1) {
            hue -= 1;
        }

        hsl.x = hue * 360;
    }

    return hsl;
}

