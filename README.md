

# Liquid Glass Calendar  
![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg desktop calendar built with JavaFX, featuring a stunning glass/blur UI, light and dark mode themes, multi-language support (English & বাংলা), event reminders, search & filter, and smooth user experience for productivity and personal scheduling.

## Table of Contents

- [Features](#features)
- [Demo](#demo)
- [Installation](#installation)
- [Usage](#usage)
- [Keyboard Shortcuts](#keyboard-shortcuts)
- [Window Controls](#window-controls)
- [System Requirements](#system-requirements)
- [Project Structure](#project-structure)
- [Build & Run](#build--run)
- [Contributing](#contributing)
- [Testing](#testing)
- [Known Issues](#known-issues)
- [Roadmap](#roadmap)
- [License](#license)
- [Contact](#contact)

## Features

- Elegant glass/blur (frosted) UI using JavaFX CSS and effects
- Basic and recurring event management (add, edit, delete)
- Event reminders with pop-up notifications
- Light, dark, blue, and green themes with instant toggle
- Multi-language support (English & বাংলা)
- Search and filter events by title
- Quick sidebar with settings and (future) import/export
- Responsive design and smooth UI transitions
- Today highlight, weekends highlight, and event color dots
- Drag & drop event moving (between days)
- Checklist and file attachment support for events
- Keyboard navigation and accessibility support
- Modular, clean JavaFX codebase

## Demo

Screenshot 2025-07-04 212443.png

1. **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/liquid-glass-calendar.git
    cd liquid-glass-calendar
    ```

2. **Ensure Java Development Kit (JDK 8+) and JavaFX SDK are installed and configured in your environment.**

3. **Set the environment variable `PATH_TO_FX` to your JavaFX SDK lib path.**  
   Example (Linux/Mac):
    ```bash
    export PATH_TO_FX=/path/to/javafx-sdk/lib
    ```

4. **Compile and run the application:**  
    ```bash
    javac --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml src/com/example/calander/LiquidGlassCalendar.java
    java --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml com.example.calander.LiquidGlassCalendar
    ```

## Usage

- Use the on-screen calendar to view, add, edit, or delete events.
- Click on any date to manage that day's events.
- Set reminders for events to get pop-up notifications.
- Use the search bar to filter events by title.
- Switch between light, dark, blue, and green themes instantly.
- Change language (English/বাংলা) from the top bar.
- Drag events from one day to another for easy rescheduling.
- Open the sidebar for settings and (future) import/export options.
- Use the checklist and attachment features inside the event modal.

## Keyboard Shortcuts

| Key/Action        | Function                           |
|-------------------|------------------------------------|
| Tab/Shift+Tab     | Navigate between UI elements       |
| Enter             | Confirm or save in dialogs         |
| Esc               | Close modals/dialogs               |
| Arrow keys        | Navigate calendar cells (future)   |
| Ctrl+F            | Focus search bar                   |
| Delete/Backspace  | Delete selected event (in modal)   |

## Window Controls

| Control       | Description                         |
|---------------|-------------------------------------|
| ☰ (Hamburger) | Open/close sidebar                  |
| ☀ / ☾         | Toggle light/dark theme             |
| Language      | Switch between English/বাংলা        |
| Settings      | Open settings dialog                |

## System Requirements

- Java Development Kit (JDK) 8 or higher
- JavaFX SDK (compatible with your JDK)
- Operating System: Windows, macOS, Linux

## Project Structure

```
liquid-glass-calendar/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── example/
│                   └── calander/
│                       └── LiquidGlassCalendar.java
├── resources/
│   └── calendar.css
├── README.md
├── LICENSE
└── .gitignore
```

## Build & Run

- Open the project in IntelliJ IDEA or any Java IDE configured with JavaFX support.
- Build the project using your IDE's build tools or manually with the commands in the [Installation](#installation) section.
- Run the `com.example.calander.LiquidGlassCalendar` class to launch the app.

## Contributing

Contributions are welcome! Please follow the steps below:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature`).
3. Make your changes and commit (`git commit -m 'Add your feature'`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Open a Pull Request.

Please ensure code follows consistent style and includes comments where necessary.

## Testing

- Currently, manual testing is recommended due to the GUI nature of the application.
- Future versions will include automated UI tests.

## Known Issues

- No Google Calendar or .ics import/export yet.
- No calculation of event overlaps or conflicts.
- No history or undo/redo for event changes.
- Limited accessibility features.

## Roadmap

- Add Google Calendar/.ics import/export.
- Add event history, undo/redo, and recurring patterns.
- Add more advanced search/filtering.
- Add accessibility and localization for more languages.
- Add mobile-friendly responsive design.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact

**Developer:** Your Name  
**GitHub:** [your-github-username](https://github.com/your-github-username)  
**Email:** your.email@example.com

Thank you for using **Liquid Glass Calendar**!  
Feel free to report issues or contribute to improve this project.

