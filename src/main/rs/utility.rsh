#pragma version(1)
#pragma rs java_package_name(newera.myapplication)

const static float3 gBlackWhiteMult = {0.299f, 0.587f, 0.114f};

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

/*
# specify h as whole input degrees (e.g 0-360)
# s = 0.0 - 1 (0 - 100%)
# l = 0.0 - 1, (0 - 100%)
# returns output from R's rgb() functin

hsl_to_rgb <- function(h, s, l) {
  h <- h / 360
  r <- g <- b <- 0.0
  if (s == 0) {
    r <- g <- b <- l
  } else {
    hue_to_rgb <- function(p, q, t) {
      if (t < 0) { t <- t + 1.0 }
      if (t > 1) { t <- t - 1.0 }
      if (t < 1/6) { return(p + (q - p) * 6.0 * t) }
      if (t < 1/2) { return(q) }
      if (t < 2/3) { return(p + ((q - p) * ((2/3) - t) * 6)) }
      return(p)
    }
    q <- ifelse(l < 0.5, l * (1.0 + s), l + s - (l*s))
    p <- 2.0 * l - q
    r <- hue_to_rgb(p, q, h + 1/3)
    g <- hue_to_rgb(p, q, h)
    b <- hue_to_rgb(p, q, h - 1/3)
  }
  return(rgb(r,g,b))
}
*/

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
      int grayscale = r * gBlackWhiteMult.r +  g * gBlackWhiteMult.g + b * gBlackWhiteMult.b;
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



/*

# r, g, b = 0.0 - 1 (0 - 100%)
# returns h/s/l in a vector, h = 0-360 deg, s = 0.0 - 1 (0-100%), l = 0.0 - 1 (0-100%)
rgb_to_hsl <- function(r, g, b) {
  val_max <- max(c(r, g, b))
  val_min <- min(c(r, g, b))
  h <- s <- l <- (val_max + val_min) / 2
  if (val_max == val_min){
    h <- s <- 0
  } else {
    d <- val_max - val_min
    s <- ifelse(l > 0.5, d / (2 - val_max - val_min), d / (val_max + val_min))
    if (val_max == r) { h <- (g - b) / d + (ifelse(g < b, 6, 0)) }
    if (val_max == g) { h <- (b - r) / d/ + 2 }
    if (val_max == b) { h <- (r - g) / d + 4 }
    h <- (h / 6) * 360
  }
  return(c(h=h, s=s, l=l))
}
*/

