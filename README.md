# Interactive Ray Tracer

An interactive 3D ray tracing application that allows real-time camera movement within a rendered scene.

## About the Project

This project extends a Java-based ray tracing API into an interactive 3D viewer with keyboard controls for camera navigation. It demonstrates the integration of a computationally intensive rendering engine with a responsive user interface, allowing users to explore 3D scenes in real-time.

### Key features

- Real-time camera movement with keyboard controls
- Progressive rendering (lower quality during movement, high quality when stationary)
- Asynchronous rendering to maintain UI responsiveness
- Status display showing rendering progress
- Detailed position information

## Technologies Used

### Backend

- Java 17
- Spring Boot 3.2.0
- Maven for dependency management
- Modular architecture:
	- `raytracer-core`: Core ray tracing engine
	- `raytracer-server`: Spring Boot REST API

### Frontend

- React 18
- TypeScript
- Vite for development and building
- Tailwind CSS for styling
- Lucide React for icons
- Radix UI for accessible components

## Project Structure

``` plaintext
raytracer-interactive/
│
├── raytracer-core/           # Core ray tracing engine
│   ├── src/main/java/cs3318/raytracing/
│   │   ├── model/           # Domain models
│   │   ├── utils/           # Utility classes
│   │   └── api/             # Public API
│
├── raytracer-server/         # Spring Boot backend
│   ├── src/main/java/cs3318/raytracing/server/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # REST endpoints
│   │   └── service/         # Business logic
│
└── raytracer-ui/            # React frontend
	├── src/
	│   ├── components/      # React components
	│   ├── lib/             # Utility functions
	│   └── services/        # API integration
```

## Prerequisites

- JDK 17 or later
- Node.js 18 or later
- npm or yarn
- Maven 3.8 or later

## Setup Instructions

1. ### Clone the Repository
	``` bash
	bashgit clone <repository-url>
	cd raytracer-interactive
	```
2. ### Backend Setup
	- Build the core raytracer and server:
		``` bash
		mvn clean install
		```
	- Run the Spring Boot server:
		``` bash
		bashcd raytracer-server
		mvn spring-boot:run
		```

3. ### Frontend Setup
	``` bash
	bashcd raytracer-ui

	# Create a virtual environment (optional but recommended)
	python -m venv venv
	source venv/bin/activate  # On Unix/macOS
	.\venv\Scripts\activate   # On Windows

	# Install dependencies
	npm install

	# Start the development server
	npm run dev
	```

## How to Use

1. Open your browser and navigate to the url shown by the frontend server upon running `npm run dev` (usually `http://localhost:` `5173` or `3000`)
2. You'll see a rendered scene with camera position information
3. Use the following keyboard controls to navigate:
	- Move Forward - `W`
	- Move Backward - `S`
	- Move Left - `A`
	- Move Right - `D`
	- Move Up - `E`
	- Move Down - `Q`
	- Look Forward - `↑`
	- Look Backward - `↓`
	- Look Left - `←`
	- Look Right - `→`
	- Look Up - `.`
	- Look Down - `,`

4. During movement, a lower quality render is used for responsiveness
5. When you stop moving, a high-quality render is generated

## Dependencies

### Backend Dependencies

- Spring Boot Starter Web
- Spring Boot Starter Test (for testing)
- Java AWT for image handling

### Frontend Dependencies

- React and React DOM
- TypeScript
- Vite
- Tailwind CSS
- Lucide React
- Radix UI components
- ESLint for code quality

## Development Notes

- The application uses throttling to prevent overwhelming the rendering engine
- Asynchronous rendering allows the UI to remain responsive
- Camera position and look-at points are synchronized between frontend and backend
- Error handling is implemented to provide feedback on rendering issues

## Acknowledgments

- Based on ray tracer originally written by Leonard McMillan for MIT 6.837 (Fall '98)
- Modernized and adapted for CS3318 Advanced Java Programming in 2024
- [More information](https://courses.cs.washington.edu/courses/cse457/03sp/applets/seeray/thesis/) about this project's background and original implementation
