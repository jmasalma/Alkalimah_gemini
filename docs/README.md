# Alkalimah Web - Arabic Flashcards

A web-based version of the Alkalimah Android app for learning Arabic words from the Quran. This application provides an interactive flashcard experience with audio pronunciation, making it easy to study the most frequent Arabic words found in the Quran.

## Features

### 🎯 Core Functionality
- **1000 Most Frequent Words**: Study the top 1000 Arabic words from the Quran
- **Interactive Flashcards**: Clean, focused interface for effective learning
- **Audio Pronunciation**: High-quality audio for each word
- **Progress Tracking**: See your current position and overall progress

### 🎨 User Experience
- **Responsive Design**: Works perfectly on desktop, tablet, and mobile devices
- **Touch & Swipe Support**: Navigate with touch gestures on mobile
- **Keyboard Navigation**: Use arrow keys and spacebar for quick navigation
- **Smooth Animations**: Elegant transitions between cards

### ⚙️ Customization
- **Transliteration Toggle**: Show/hide phonetic pronunciation
- **Auto-play Audio**: Automatically play pronunciation when showing new words
- **Settings Persistence**: Your preferences are saved locally

### 🌐 Web Technologies
- **Pure HTML/CSS/JavaScript**: No frameworks required
- **Local Storage**: Settings and progress saved in browser
- **GitHub Pages Ready**: Easy deployment and hosting

## Usage

### Navigation
- **Next/Previous Buttons**: Click to navigate between words
- **Keyboard Shortcuts**:
  - `←` / `→` Arrow keys: Navigate between words
  - `Space`: Play audio pronunciation
  - `Escape`: Close settings panel
- **Touch Gestures**: Swipe left/right on mobile devices

### Settings
Click the settings button (⚙️) in the bottom-right corner to:
- Toggle transliteration display
- Enable/disable auto-play audio
- Access additional options

## File Structure

```
alkalimah-web/
├── index.html              # Main application page
├── css/
│   └── styles.css          # Responsive styling
├── js/
│   └── app.js              # Application logic
├── data/
│   └── words.json          # Word database (1000 entries)
├── audio/                  # Audio pronunciation files
│   ├── word_1.mp3
│   ├── word_2.mp3
│   └── ... (1000 files)
├── extract_data.py         # Database extraction script
└── README.md               # This file
```

## Data Source

The word data is extracted from the original Android app's SQLite database, containing:
- **Arabic Text**: Uthmani script (as found in the Quran)
- **English Translation**: Meaning of each word
- **Transliteration**: Phonetic pronunciation guide
- **Audio Files**: MP3 pronunciation for each word
- **Frequency Data**: How often each word appears in the Quran

## GitHub Pages Deployment

This application is designed to work seamlessly with GitHub Pages:

1. **Automatic Deployment**: Push changes to the main branch
2. **Custom Domain Support**: Can be configured with your own domain
3. **HTTPS**: Secure connection by default
4. **Global CDN**: Fast loading worldwide

### Setup Instructions

1. Enable GitHub Pages in your repository settings
2. Set source to "Deploy from a branch"
3. Select the main branch and `/docs` folder
4. Your app will be available at: `https://[username].github.io/[repository]/`

## Browser Compatibility

- **Modern Browsers**: Chrome, Firefox, Safari, Edge (latest versions)
- **Mobile Browsers**: iOS Safari, Chrome Mobile, Samsung Internet
- **Features Used**: ES6+, Fetch API, Web Audio API, CSS Grid/Flexbox

## Development

### Local Development
1. Clone the repository
2. Navigate to the `alkalimah-web` directory
3. Serve the files using a local web server:
   ```bash
   # Using Python
   python -m http.server 8000
   
   # Using Node.js
   npx serve .
   
   # Using PHP
   php -S localhost:8000
   ```
4. Open `http://localhost:8000` in your browser

### Data Extraction
To regenerate the word data from the Android app's database:
```bash
cd alkalimah-web
python3 extract_data.py
```

## Performance

- **Initial Load**: ~2-3 seconds (includes 1000 audio files)
- **Navigation**: Instant (data preloaded)
- **Audio Playback**: <100ms response time
- **Mobile Optimized**: Touch-friendly interface

## Future Enhancements

- [ ] **Search Functionality**: Find specific words
- [ ] **Study Modes**: Random order, spaced repetition
- [ ] **Progress Analytics**: Learning statistics and insights
- [ ] **Offline Support**: Service Worker for offline usage
- [ ] **Bookmarking**: Save favorite or difficult words
- [ ] **Themes**: Dark mode and color customization

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly on different devices
5. Submit a pull request

## License

This project maintains the same license as the original Alkalimah Android application.

## Acknowledgments

- **Original App**: Based on the Alkalimah Android application
- **Fonts**: Amiri (Arabic) and Inter (Latin) from Google Fonts
- **Icons**: Material Design icons

---

**Live Demo**: [View the application](https://jmasalma.github.io/Alkalimah/)

For questions or support, please open an issue in the repository.
