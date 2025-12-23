/**
 * Alkalimah Web App - Main JavaScript
 * Arabic Flashcard Learning Application
 */

class AlkalimahApp {
    constructor() {
        this.words = [];
        this.currentIndex = 0;
        this.settings = {
            showTransliteration: false,
            autoPlay: false,
            randomOrder: false
        };
        this.audioContext = null;
        this.currentAudio = null;
        
        this.init();
    }

    async init() {
        try {
            // Load settings from localStorage
            this.loadSettings();
            
            // Load word data
            await this.loadWords();
            
            // Initialize UI
            this.initializeUI();
            
            // Set up event listeners
            this.setupEventListeners();
            
            // Display first word
            this.displayCurrentWord();
            
            // Hide loading overlay
            this.hideLoading();
            
        } catch (error) {
            console.error('Failed to initialize app:', error);
            this.showError('Failed to load the application. Please refresh the page.');
        }
    }

    async loadWords() {
        try {
            const response = await fetch('data/words.json');
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json();
            this.words = data.words;
            
            if (this.words.length === 0) {
                throw new Error('No words found in the database');
            }
            
            // Apply randomization if enabled
            if (this.settings.randomOrder) {
                this.shuffleWords();
            }
            
            console.log(`Loaded ${this.words.length} words`);
        } catch (error) {
            console.error('Error loading words:', error);
            throw error;
        }
    }

    loadSettings() {
        const savedSettings = localStorage.getItem('alkalimah-settings');
        if (savedSettings) {
            this.settings = { ...this.settings, ...JSON.parse(savedSettings) };
        }
    }

    saveSettings() {
        localStorage.setItem('alkalimah-settings', JSON.stringify(this.settings));
    }

    initializeUI() {
        // Set up settings toggles
        const transliterationToggle = document.getElementById('transliteration-toggle');
        const autoPlayToggle = document.getElementById('auto-play-toggle');
        const randomOrderToggle = document.getElementById('random-order-toggle');
        
        transliterationToggle.checked = this.settings.showTransliteration;
        autoPlayToggle.checked = this.settings.autoPlay;
        randomOrderToggle.checked = this.settings.randomOrder;
        
        // Update transliteration visibility
        this.updateTransliterationVisibility();
    }

    setupEventListeners() {
        // Navigation buttons
        document.getElementById('prev-btn').addEventListener('click', () => this.previousWord());
        document.getElementById('next-btn').addEventListener('click', () => this.nextWord());
        
        // Audio button
        document.getElementById('audio-button').addEventListener('click', () => this.playAudio());
        
        // Settings
        document.getElementById('settings-btn').addEventListener('click', () => this.toggleSettings());
        document.getElementById('transliteration-toggle').addEventListener('change', (e) => {
            this.settings.showTransliteration = e.target.checked;
            this.updateTransliterationVisibility();
            this.saveSettings();
        });
        document.getElementById('auto-play-toggle').addEventListener('change', (e) => {
            this.settings.autoPlay = e.target.checked;
            this.saveSettings();
        });
        document.getElementById('random-order-toggle').addEventListener('change', (e) => {
            this.settings.randomOrder = e.target.checked;
            this.saveSettings();
            
            // Apply or remove randomization immediately
            if (e.target.checked) {
                this.shuffleWords();
            } else {
                // Restore original order by reloading
                this.loadWords();
            }
            
            // Reset to first word
            this.currentIndex = 0;
            this.displayCurrentWord();
        });
        
        // Keyboard navigation
        document.addEventListener('keydown', (e) => this.handleKeyboard(e));
        
        // Close settings when clicking outside
        document.addEventListener('click', (e) => {
            const settingsPanel = document.getElementById('settings-panel');
            const settingsBtn = document.getElementById('settings-btn');
            
            if (!settingsPanel.contains(e.target) && !settingsBtn.contains(e.target)) {
                settingsPanel.classList.remove('open');
            }
        });
        
        // Touch/swipe support for mobile
        this.setupTouchEvents();
    }

    setupTouchEvents() {
        const flashcard = document.getElementById('flashcard');
        let startX = 0;
        let startY = 0;
        
        flashcard.addEventListener('touchstart', (e) => {
            startX = e.touches[0].clientX;
            startY = e.touches[0].clientY;
        });
        
        flashcard.addEventListener('touchend', (e) => {
            if (!startX || !startY) return;
            
            const endX = e.changedTouches[0].clientX;
            const endY = e.changedTouches[0].clientY;
            
            const diffX = startX - endX;
            const diffY = startY - endY;
            
            // Only handle horizontal swipes
            if (Math.abs(diffX) > Math.abs(diffY) && Math.abs(diffX) > 50) {
                if (diffX > 0) {
                    // Swipe left - next word
                    this.nextWord();
                } else {
                    // Swipe right - previous word
                    this.previousWord();
                }
            }
            
            startX = 0;
            startY = 0;
        });
    }

    handleKeyboard(e) {
        switch (e.key) {
            case 'ArrowLeft':
                e.preventDefault();
                this.previousWord();
                break;
            case 'ArrowRight':
                e.preventDefault();
                this.nextWord();
                break;
            case ' ':
                e.preventDefault();
                this.playAudio();
                break;
            case 'Escape':
                document.getElementById('settings-panel').classList.remove('open');
                break;
        }
    }

    displayCurrentWord() {
        if (this.words.length === 0) return;
        
        const word = this.words[this.currentIndex];
        
        // Update word display
        document.getElementById('word-arabic').textContent = word.uthmani || '';
        document.getElementById('word-translation').textContent = word.translation || '';
        document.getElementById('word-transliteration').textContent = word.transliteration || '';
        
        // Update progress
        document.getElementById('progress-text').textContent = `${this.currentIndex + 1} / ${this.words.length}`;
        
        // Update navigation buttons
        document.getElementById('prev-btn').disabled = this.currentIndex === 0;
        document.getElementById('next-btn').textContent = this.currentIndex === this.words.length - 1 ? 'Restart' : 'Next';
        
        // Enable/disable audio button
        const audioButton = document.getElementById('audio-button');
        audioButton.disabled = !word.audio_file;
        
        // Auto-play audio if enabled
        if (this.settings.autoPlay && word.audio_file) {
            setTimeout(() => this.playAudio(), 500);
        }
        
        // Add fade-in animation
        const flashcard = document.getElementById('flashcard');
        flashcard.classList.remove('fade-out');
        flashcard.classList.add('fade-in');
    }

    async playAudio() {
        const word = this.words[this.currentIndex];
        if (!word.audio_file) return;
        
        const audioButton = document.getElementById('audio-button');
        const playIcon = audioButton.querySelector('.play-icon');
        const audioText = audioButton.querySelector('.audio-text');
        
        try {
            // Stop current audio if playing
            if (this.currentAudio) {
                this.currentAudio.pause();
                this.currentAudio.currentTime = 0;
            }
            
            // Update button state
            audioButton.disabled = true;
            audioText.textContent = 'Playing...';
            playIcon.style.opacity = '0.6';
            
            // Create and play new audio
            this.currentAudio = new Audio(word.audio_file);
            
            this.currentAudio.addEventListener('ended', () => {
                this.resetAudioButton();
            });
            
            this.currentAudio.addEventListener('error', (e) => {
                console.error('Audio playback error:', e);
                this.resetAudioButton();
            });
            
            await this.currentAudio.play();
            
        } catch (error) {
            console.error('Error playing audio:', error);
            this.resetAudioButton();
        }
    }

    resetAudioButton() {
        const audioButton = document.getElementById('audio-button');
        const playIcon = audioButton.querySelector('.play-icon');
        const audioText = audioButton.querySelector('.audio-text');
        
        audioButton.disabled = false;
        audioText.textContent = 'Play Audio';
        playIcon.style.opacity = '1';
    }

    previousWord() {
        if (this.currentIndex > 0) {
            this.animateTransition(() => {
                this.currentIndex--;
                this.displayCurrentWord();
            });
        }
    }

    nextWord() {
        if (this.currentIndex < this.words.length - 1) {
            this.animateTransition(() => {
                this.currentIndex++;
                this.displayCurrentWord();
            });
        } else {
            // Restart from beginning
            this.animateTransition(() => {
                this.currentIndex = 0;
                this.displayCurrentWord();
            });
        }
    }

    animateTransition(callback) {
        const flashcard = document.getElementById('flashcard');
        flashcard.classList.add('fade-out');
        
        setTimeout(() => {
            callback();
            flashcard.classList.remove('fade-out');
        }, 150);
    }

    updateTransliterationVisibility() {
        const transliterationElement = document.getElementById('word-transliteration');
        transliterationElement.style.display = this.settings.showTransliteration ? 'block' : 'none';
    }

    toggleSettings() {
        const settingsPanel = document.getElementById('settings-panel');
        settingsPanel.classList.toggle('open');
    }

    hideLoading() {
        const loadingOverlay = document.getElementById('loading-overlay');
        loadingOverlay.classList.add('hidden');
    }

    showError(message) {
        const loadingOverlay = document.getElementById('loading-overlay');
        loadingOverlay.innerHTML = `
            <div style="text-align: center;">
                <h2 style="color: #e53e3e; margin-bottom: 1rem;">Error</h2>
                <p style="color: #4a5568; margin-bottom: 2rem;">${message}</p>
                <button onclick="location.reload()" style="
                    background: #667eea;
                    color: white;
                    border: none;
                    padding: 0.8rem 2rem;
                    border-radius: 8px;
                    cursor: pointer;
                    font-size: 1rem;
                ">Reload Page</button>
            </div>
        `;
    }

    // Utility method to get current word info
    getCurrentWordInfo() {
        if (this.words.length === 0) return null;
        
        const word = this.words[this.currentIndex];
        return {
            index: this.currentIndex,
            total: this.words.length,
            word: word,
            progress: ((this.currentIndex + 1) / this.words.length * 100).toFixed(1)
        };
    }

    // Method to jump to a specific word by index
    goToWord(index) {
        if (index >= 0 && index < this.words.length) {
            this.animateTransition(() => {
                this.currentIndex = index;
                this.displayCurrentWord();
            });
        }
    }

    // Method to shuffle words array
    shuffleWords() {
        for (let i = this.words.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [this.words[i], this.words[j]] = [this.words[j], this.words[i]];
        }
    }

    // Method to search for words (future enhancement)
    searchWords(query) {
        return this.words.filter(word => 
            (word.simple && word.simple.includes(query)) ||
            (word.translation && word.translation.toLowerCase().includes(query.toLowerCase())) ||
            (word.transliteration && word.transliteration.toLowerCase().includes(query.toLowerCase()))
        );
    }
}

// Initialize the app when the DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    window.alkalimahApp = new AlkalimahApp();
});

// Service Worker registration for offline support (future enhancement)
if ('serviceWorker' in navigator) {
    window.addEventListener('load', () => {
        navigator.serviceWorker.register('sw.js')
            .then(registration => {
                console.log('SW registered: ', registration);
            })
            .catch(registrationError => {
                console.log('SW registration failed: ', registrationError);
            });
    });
}
