#!/usr/bin/env python3
"""
Extract data from Alkalimah SQLite database and convert to web formats
- Export word data to JSON
- Extract audio blobs to MP3 files
"""

import sqlite3
import json
import os
from pathlib import Path

def extract_alkalimah_data():
    # Database path
    db_path = "../app/src/main/assets/databases/alkalimah_words.db"
    
    # Output directories
    audio_dir = Path("audio")
    data_dir = Path("data")
    
    # Ensure directories exist
    audio_dir.mkdir(exist_ok=True)
    data_dir.mkdir(exist_ok=True)
    
    # Connect to database
    try:
        conn = sqlite3.connect(db_path)
        cursor = conn.cursor()
        
        # Query all words
        cursor.execute("""
            SELECT id, location, uthmani, simple, translation, transliteration, locations, audio_blob
            FROM words
            ORDER BY locations DESC, id ASC
        """)
        
        words_data = []
        audio_count = 0
        
        for row in cursor.fetchall():
            word_id, location, uthmani, simple, translation, transliteration, locations, audio_blob = row
            
            # Create word data object
            word_data = {
                "id": word_id,
                "location": location,
                "uthmani": uthmani,
                "simple": simple,
                "translation": translation,
                "transliteration": transliteration,
                "locations": locations,
                "audio_file": None
            }
            
            # Extract audio blob to MP3 file if it exists
            if audio_blob:
                audio_filename = f"word_{word_id}.mp3"
                audio_path = audio_dir / audio_filename
                
                try:
                    with open(audio_path, 'wb') as audio_file:
                        audio_file.write(audio_blob)
                    word_data["audio_file"] = f"audio/{audio_filename}"
                    audio_count += 1
                    print(f"Extracted audio for word {word_id}: {simple}")
                except Exception as e:
                    print(f"Error extracting audio for word {word_id}: {e}")
            
            words_data.append(word_data)
        
        # Save words data to JSON
        json_path = data_dir / "words.json"
        with open(json_path, 'w', encoding='utf-8') as json_file:
            json.dump({
                "words": words_data,
                "total_words": len(words_data),
                "total_audio_files": audio_count
            }, json_file, ensure_ascii=False, indent=2)
        
        print(f"\nExtraction complete!")
        print(f"Total words: {len(words_data)}")
        print(f"Audio files extracted: {audio_count}")
        print(f"JSON data saved to: {json_path}")
        
        conn.close()
        
    except sqlite3.Error as e:
        print(f"Database error: {e}")
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    extract_alkalimah_data()
