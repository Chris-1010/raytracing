// src/components/InteractiveRayTracer.tsx
import { useState, useEffect, useRef, useCallback } from 'react';
import debounce from 'lodash/debounce';
import { Camera, AlertCircle } from 'lucide-react';
import { Alert, AlertDescription } from './ui/alert';
import { rayTraceService } from '../services/rayTraceService';

type CameraPosition = {
    x: number;
    y: number;
    z: number;
};

type LookAtPoint = {
    x: number;
    y: number;
    z: number;
};

export default function InteractiveRayTracer() {
    // Initial default values
    const defaultCamera = { x: -1.4, y: 0.3, z: 7 };
    const defaultLookAt = { x: -0.5, y: 0.7, z: -12 };

    const [cameraPosition, setCameraPosition] = useState(defaultCamera);
    const [lookAtPoint, setLookAtPoint] = useState(defaultLookAt);
    const [isRendering, setIsRendering] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [lastRenderTime, setLastRenderTime] = useState(0);
    const imageRef = useRef<HTMLImageElement>(null);

    const MOVE_SPEED = 0.5;
    const RENDER_THROTTLE = 100;    // Throttle time in milliseconds - 100ms recommended - Time to wait between renders
    const DEBOUNCE_TIME = 500;    // Debounce time in milliseconds - 150ms recommended - Time to wait after last keypress before sending request
    const POLLING_INTERVAL = 100; // Polling interval in milliseconds - 1000ms recommended - Time to wait between checking render status to ensure render is complete and not in progress

    const updateCamera = async (newPosition: CameraPosition, newLookAt: LookAtPoint, quickRender = true) => {
        try {
            await rayTraceService.updateCamera({
                eyeX: newPosition.x,
                eyeY: newPosition.y,
                eyeZ: newPosition.z,
                lookAtX: newLookAt.x,
                lookAtY: newLookAt.y,
                lookAtZ: newLookAt.z
            }, quickRender);
            setError(null);
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to update camera position');
        }
    };

    const fetchRender = async () => {
        try {
            const blob = await rayTraceService.getCurrentRender();
            const imageUrl = URL.createObjectURL(blob);
            if (imageRef.current) {
                imageRef.current.src = imageUrl;
            }
            setError(null);
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to fetch render');
        }
    };

    useEffect(() => {
        let pollInterval: number;

        const pollRenderStatus = async () => {
            try {
                const isCurrentlyRendering = await rayTraceService.getRenderStatus();
                setIsRendering(isCurrentlyRendering);

                if (!isCurrentlyRendering) {
                    await fetchRender();
                }
            } catch (err) {
                setError(err instanceof Error ? err.message : 'Failed to check render status');
            }
        };

        pollInterval = window.setInterval(pollRenderStatus, POLLING_INTERVAL);
        return () => window.clearInterval(pollInterval);
    }, []);

    useEffect(() => {
        const handleKeyPress = async (e: KeyboardEvent) => {
            const now = Date.now();
            if (now - lastRenderTime < RENDER_THROTTLE) return;

            let newPosition = { ...cameraPosition };
            let newLookAt = { ...lookAtPoint };

            switch(e.key.toLowerCase()) {
                case 'w':
                    newPosition.z += MOVE_SPEED;
                    break;
                case 's':
                    newPosition.z -= MOVE_SPEED;
                    break;
                case 'a':
                    newPosition.x -= MOVE_SPEED;
                    break;
                case 'd':
                    newPosition.x += MOVE_SPEED;
                    break;
                case 'q':
                    newPosition.y -= MOVE_SPEED;
                    break;
                case 'e':
                    newPosition.y += MOVE_SPEED;
                    break;
                case 'arrowup':
                    newLookAt.z += MOVE_SPEED;
                    break;
                case 'arrowdown':
                    newLookAt.z -= MOVE_SPEED;
                    break;
                case 'arrowleft':
                    newLookAt.x -= MOVE_SPEED;
                    break;
                case 'arrowright':
                    newLookAt.x += MOVE_SPEED;
                    break;
                case '.':
                    newLookAt.y += MOVE_SPEED;
                    break;
                case ',':
                    newLookAt.y -= MOVE_SPEED;
                    break;
                default:
                    return;
            }

            setCameraPosition(newPosition);
            setLookAtPoint(newLookAt);
            setLastRenderTime(now);

            debouncedUpdateCamera(newPosition, newLookAt, true);
        };

        const handleKeyUp = async () => {
            await updateCamera(cameraPosition, lookAtPoint, false);
        };

        window.addEventListener('keydown', handleKeyPress);
        window.addEventListener('keyup', handleKeyUp);
        return () => {
            window.removeEventListener('keydown', handleKeyPress);
            window.removeEventListener('keyup', handleKeyUp);
            debouncedUpdateCamera.cancel(); // Cancel any pending updates
        };
    }, [cameraPosition, lookAtPoint, lastRenderTime]);

    const debouncedUpdateCamera = useCallback(
        debounce((newPosition: CameraPosition, newLookAt: LookAtPoint, quickRender: boolean) => {
            updateCamera(newPosition, newLookAt, quickRender);
        }, DEBOUNCE_TIME),
        []
    );

    const handlePositionChange = useCallback(
        debounce((field: string, value: string, isCamera: boolean) => {
            const numValue = parseFloat(value);
            if (isNaN(numValue)) return;

            if (isCamera) {
                setCameraPosition(prev => ({
                    ...prev,
                    [field]: numValue
                }));
                updateCamera(
                    { ...cameraPosition, [field]: numValue },
                    lookAtPoint,
                    false
                );
            } else {
                setLookAtPoint(prev => ({
                    ...prev,
                    [field]: numValue
                }));
                updateCamera(
                    cameraPosition,
                    { ...lookAtPoint, [field]: numValue },
                    false
                );
            }
        }, 500),
        [cameraPosition, lookAtPoint]
    );

    return (
        <div className="min-h-screen w-screen bg-black p-4 flex">
            {/* Left side - Scene */}
            <div className="w-2/3 pr-4 flex flex-col">
                <div className="mb-4 flex items-center justify-between">
                    <div className="flex items-center space-x-2">
                        <Camera className="text-blue-500"/>
                        <span className="font-semibold text-white">Interactive Ray Tracer</span>
                    </div>
                    {isRendering && (
                        <span className="text-sm text-orange-500">Rendering...</span>
                    )}
                </div>

                {error && (
                    <Alert variant="destructive" className="mb-4">
                        <AlertCircle className="h-4 w-4"/>
                        <AlertDescription>{error}</AlertDescription>
                    </Alert>
                )}

                <div className="bg-gray-900 p-4 rounded-lg shadow-md justify-center grow">
                    <div className="aspect-video rounded-md h-full w-full flex justify-center">
                        <img
                            ref={imageRef}
                            alt="Ray traced scene"
                            className="h-full object-contain rounded-3xl self-center"
                            src="/api/raytracer/render"
                        />
                    </div>
                </div>
            </div>

            {/* Right side - Controls */}
            <div className="w-1/3 bg-gray-900 p-4 rounded-lg">
                <div className="grid grid-cols-2 gap-4 mb-4">
                    <div className="space-y-2">
                        <h3 className="text-sm font-semibold text-white">Camera Position</h3>
                        <div className="grid grid-cols-3 gap-2">
                            <div>
                                <label className="block text-xs mb-1">X</label>
                                <input
                                    type="number"
                                    step="0.5"
                                    value={cameraPosition.x}
                                    onChange={(e) => handlePositionChange('x', e.target.value, true)}
                                    className="w-full px-2 py-1 border rounded text-sm"
                                />
                            </div>
                            <div>
                                <label className="block text-xs mb-1">Y</label>
                                <input
                                    type="number"
                                    step="0.5"
                                    value={cameraPosition.y}
                                    onChange={(e) => handlePositionChange('y', e.target.value, true)}
                                    className="w-full px-2 py-1 border rounded text-sm"
                                />
                            </div>
                            <div>
                                <label className="block text-xs mb-1">Z</label>
                                <input
                                    type="number"
                                    step="0.5"
                                    value={cameraPosition.z}
                                    onChange={(e) => handlePositionChange('z', e.target.value, true)}
                                    className="w-full px-2 py-1 border rounded text-sm"
                                />
                            </div>
                        </div>
                    </div>

                    <div className="space-y-2">
                        <h3 className="text-sm font-semibold text-white">Look At Point</h3>
                        <div className="grid grid-cols-3 gap-2">
                            <div>
                                <label className="block text-xs mb-1">X</label>
                                <input
                                    type="number"
                                    step="0.5"
                                    value={lookAtPoint.x}
                                    onChange={(e) => handlePositionChange('x', e.target.value, false)}
                                    className="w-full px-2 py-1 border rounded text-sm"
                                />
                            </div>
                            <div>
                                <label className="block text-xs mb-1">Y</label>
                                <input
                                    type="number"
                                    step="0.5"
                                    value={lookAtPoint.y}
                                    onChange={(e) => handlePositionChange('y', e.target.value, false)}
                                    className="w-full px-2 py-1 border rounded text-sm"
                                />
                            </div>
                            <div>
                                <label className="block text-xs mb-1">Z</label>
                                <input
                                    type="number"
                                    step="0.5"
                                    value={lookAtPoint.z}
                                    onChange={(e) => handlePositionChange('z', e.target.value, false)}
                                    className="w-full px-2 py-1 border rounded text-sm"
                                />
                            </div>
                        </div>
                    </div>
                </div>

                <div className="bg-gray-800 p-3 rounded-md">
                    <h3 className="text-sm font-semibold mb-2 text-white">Controls</h3>
                    <div className="grid grid-cols-2 gap-2 text-sm">
                        <div className="flex items-center space-x-2">
                            <kbd className="px-2 py-1 bg-gray-700 text-gray-200 rounded">W</kbd>
                            <span className="text-gray-300">Move Forward</span>
                        </div>
                        <div className="flex items-center space-x-2">
                            <kbd className="px-2 py-1 bg-gray-700 text-gray-200 rounded">S</kbd>
                            <span className="text-gray-300">Move Backward</span>
                        </div>
                        <div className="flex items-center space-x-2">
                            <kbd className="px-2 py-1 bg-gray-700 text-gray-200 rounded">A</kbd>
                            <span className="text-gray-300">Move Left</span>
                        </div>
                        <div className="flex items-center space-x-2">
                            <kbd className="px-2 py-1 bg-gray-700 text-gray-200 rounded">D</kbd>
                            <span className="text-gray-300">Move Right</span>
                        </div>
                        <div className="flex items-center space-x-2">
                            <kbd className="px-2 py-1 bg-gray-700 text-gray-200 rounded">E</kbd>
                            <span className="text-gray-300">Move Up</span>
                        </div>
                        <div className="flex items-center space-x-2">
                            <kbd className="px-2 py-1 bg-gray-700 text-gray-200 rounded">Q</kbd>
                            <span className="text-gray-300">Move Down</span>
                        </div>
                        <div className="flex items-center space-x-2">
                            <kbd className="px-2 py-1 bg-gray-700 text-gray-200 rounded">↑</kbd>
                            <span className="text-gray-300">Look Forward</span>
                        </div>
                        <div className="flex items-center space-x-2">
                            <kbd className="px-2 py-1 bg-gray-700 text-gray-200 rounded">↓</kbd>
                            <span className="text-gray-300">Look Backward</span>
                        </div>
                        <div className="flex items-center space-x-2">
                            <kbd className="px-2 py-1 bg-gray-700 text-gray-200 rounded">←</kbd>
                            <span className="text-gray-300">Look Left</span>
                        </div>
                        <div className="flex items-center space-x-2">
                            <kbd className="px-2 py-1 bg-gray-700 text-gray-200 rounded">→</kbd>
                            <span className="text-gray-300">Look Right</span>
                        </div>
                        <div className="flex items-center space-x-2">
                            <kbd className="px-2 py-1 bg-gray-700 text-gray-200 rounded">.</kbd>
                            <span className="text-gray-300">Look Up</span>
                        </div>
                        <div className="flex items-center space-x-2">
                            <kbd className="px-2 py-1 bg-gray-700 text-gray-200 rounded">,</kbd>
                            <span className="text-gray-300">Look Down</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}