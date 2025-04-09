// src/services/rayTraceService.ts
type CameraUpdateParams = {
    eyeX: number;
    eyeY: number;
    eyeZ: number;
    lookAtX: number;
    lookAtY: number;
    lookAtZ: number;
};

// type Dimensions = {
//     width: number;
//     height: number;
// };

export const rayTraceService = {
    updateCamera: async (params: CameraUpdateParams, quickRender: boolean = true) => {
        const searchParams = new URLSearchParams({
            eyeX: params.eyeX.toString(),
            eyeY: params.eyeY.toString(),
            eyeZ: params.eyeZ.toString(),
            lookAtX: params.lookAtX.toString(),
            lookAtY: params.lookAtY.toString(),
            lookAtZ: params.lookAtZ.toString(),
            quickRender: quickRender.toString()
        });

        const response = await fetch(`/api/raytracer/camera?${searchParams}`, {
            method: 'POST',
        });

        if (!response.ok) throw new Error('Failed to update camera position');
        return response.text();
    },

    getRenderStatus: async () => {
        const response = await fetch('/api/raytracer/status');
        if (!response.ok) throw new Error('Failed to get render status');
        return response.json();
    },

    getCurrentRender: async () => {
        const response = await fetch('/api/raytracer/render');
        if (!response.ok) throw new Error('Failed to get current render');
        return response.blob();
    }
};