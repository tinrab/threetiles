#version 120

uniform sampler2D tex;

void main(){
	vec4 color = texture2D(tex, gl_TexCoord[0].st);
	vec4 sum = vec4(0);
	for(int i = -2; i <= 2; i++) {
		for(int j = -2; j <= 2; j++){
			vec2 offset = vec2(i, j) * 0.0015;
			sum += texture2D(tex, gl_TexCoord[0].st + offset);
		}
	}
	gl_FragColor = (sum / 28) + color;
}