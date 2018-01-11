/// <reference path="../node_modules/babylonjs/babylon.d.ts" />

class PiViz {
    private canvas: HTMLCanvasElement;
    private engine: BABYLON.Engine;
    private scene: BABYLON.Scene;
    private camera: BABYLON.FreeCamera;
    private light: BABYLON.Light;

    // Create canvas and engine
    constructor(canvasElement: HTMLCanvasElement) {
        this.canvas = canvasElement;
        this.engine = new BABYLON.Engine(this.canvas, true);
    }

    createScene(): void {
        // We need a scene to create all our geometry and babylonjs items in
        this.scene = new BABYLON.Scene(this.engine);
        // Create a camera, and set its position to slightly behind our meshes
        this.camera = new BABYLON.FreeCamera('freeCamera', new BABYLON.Vector3(0, 5, -10), this.scene);
        // Make our camera look at the middle of the scene, where we have placed our items
        this.camera.setTarget(BABYLON.Vector3.Zero());
        // Attach the camera to the canvas, this allows us to give input to the camera
        this.camera.attachControl(this.canvas, false);
        // Create lightning in our scene
        this.light = new BABYLON.HemisphericLight('skyLight', new BABYLON.Vector3(0, 1, 0), this.scene);
        // Finally time to add some meshes
        // Create sphere shape and place it above ground
        let sphere = BABYLON.MeshBuilder.CreateSphere('sphere', { segments: 16, diameter: 2 }, this.scene);
        sphere.position.y = 1; //not a magic number, but half or our diameter and height
        // Make a plane on the ground
        let ground = BABYLON.MeshBuilder.CreateGround('groundPlane', { width: 6, height: 6, subdivisions: 2 }, this.scene);
    }

    resizeScene() {
        this.engine.resize();
    }

    run(): void {
        this.engine.runRenderLoop(() => {
            this.scene.render();
        });
    }
}


// Create our piviz class using the render canvas element
const canvasElementName = 'renderCanvas';
const canvasElement = document.getElementById(canvasElementName);
var canvasElement2 = <HTMLCanvasElement>canvasElement;
canvasElement2 = canvasElement as HTMLCanvasElement;
let piviz = new PiViz(canvasElement2);

// Listen for browser/canvas resize events
window.addEventListener("resize", () => {
    piviz.resizeScene();
});

// Create the scene
piviz.createScene();

// start animation
piviz.run();