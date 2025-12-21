
# Alkalimah


## ncrement tag

```bash

git checkout main
git pull --prune
latest_tag=$(git describe --abbrev=0 --tags)
new_tag=$(echo "$latest_tag" | awk -F'.' -v OFS='.' '{$NF++; print $0}')
echo Moving tag from ${latest_tag} to ${new_tag}

git tag ${new_tag}
git push origin --tags

```

## Generate data


### Download data

```bash

# Create the following python script to download all data

"""

Alkalimah app


# rm alkalimah_words.db


time python3 x.py | tee x.out
281 minutes

sqlite3 alkalimah_words.db -list -separator "|" -header "select * from words;" | column -t -s "|"

sqlite3 alkalimah_words.db -list -separator "|" -header "select writefile('x.mp3', audio_blob) from words where location = '100:6:2';" | column -t -s "|"

# Pick top words by frequency, in app, move to less common words in more advanced modes
sqlite3 alkalimah_words.db -list -separator "|" -header "select simple, count(location) as locations from words group by simple order by locations desc;" | column -t -s "|"


select
simple, count(location) as locations
from words
where length(simple) > 2
group by simple
order by locations desc;


select
*
from words 
where simple = "تعلمون"
order by id desc
limit 1
;


"""

# pip install requests beautifulsoup4
# pip install lxml


import sqlite3
import requests
import json
import traceback
from bs4 import BeautifulSoup
from pprint import pprint

# max_surah_no = 114
start_surah = 3 # 1
end_surah = 114 # 114
max_ayah_no = 300

# surah_no = 1
# ayah_no = 11
page_url = "https://quran.com"
audio_url = "https://audio.qurancdn.com"



# Create database connection to a new sqlite3 DB and create a table
conn = sqlite3.connect('alkalimah_words.db')
c = conn.cursor()
c.execute('''CREATE TABLE IF NOT EXISTS words
             (
              id INTEGER PRIMARY KEY AUTOINCREMENT,
              location TEXT,
              uthmani TEXT,
              simple TEXT,
              translation TEXT,
              transliteration TEXT,
              audio_blob BLOB)''')

for surah_no in range(start_surah, end_surah + 1):
    last_ayah = False
    for ayah_no in range(1, max_ayah_no + 1):
        if last_ayah:
            break
        url = f"{page_url}/{surah_no}/{ayah_no}"
        response = requests.get(url)
        html_content = response.text
        soup = BeautifulSoup(html_content, "lxml")
        # pprint(f"Fetching Surah {surah_no}, Ayah {ayah_no} from {url}")
        # pprint(f"Response Status Code: {response.status_code}")
        # pprint(f"html_content: {html_content}")
        # exit()
        # if 'Sorry, something went wrong' in html_content:
        #     # print("Error: Something went wrong while fetching the page.")
        #     # exit()
        #     # continue to next ayah
        #     break

        try:
            for script in soup.find_all('script', id='__NEXT_DATA__', type='application/json'):
                # pprint(f"Processing script data for Surah {surah_no}, Ayah {ayah_no}")
                json_data = script.string
                data = json.loads(json_data)
                for verses in data.get('props').get('pageProps').get('versesResponse').get('verses'):
                    # pprint(f"Processing verse data for Surah {surah_no}, Ayah {ayah_no}")
                    # pprint(verses)
                    for word in verses.get('words'):
                        if word.get('charTypeName') != 'word':
                            continue
                        # pprint(f"Processing word data for Surah {surah_no}, Ayah {ayah_no}")
                        word_location = word.get('location')
                        word_uthmani = word.get('textUthmani')
                        word_simple = word.get('textImlaeiSimple')
                        word_translation_text = word.get('translation').get('text') if word.get('translation') else None
                        word_transliteration_text = word.get('transliteration').get('text') if word.get('transliteration') else None
                        word_audio = f"{audio_url}/{word.get('audioUrl')}"
                        print(f"Location: {word_location} - Uthmani: {word_uthmani} - Simple: {word_simple} - Translation: {word_translation_text} - Transliteration: {word_transliteration_text} - Audio: {word_audio}")
                        # Fetch audio blob
                        audio_response = requests.get(word_audio)
                        audio_blob = audio_response.content if audio_response.status_code == 200 else None
                        # Insert into database
                        c.execute('''INSERT OR REPLACE INTO words (location, uthmani, simple, translation, transliteration, audio_blob)
                                     VALUES (?, ?, ?, ?, ?, ?)''',
                                  (word_location, word_uthmani, word_simple, word_translation_text, word_transliteration_text, audio_blob))
                        # pprint(word)
                        # print("-----")
                        # exit()
        except Exception as e:
            # pprint(f"Error processing Surah {surah_no}, Ayah {ayah_no}: {e}")
            # traceback.print_exc()
            # continue to next surah ???

            # exit()
            last_ayah = True
            continue
    # Commit every surah
    conn.commit()

conn.commit()
conn.close()


```


### Process data

```sql

sqlite3 alkalimah_words.db


ATTACH DATABASE 'alkalimah_words_1k.db' AS target_db;

CREATE TABLE target_db.words (
id INTEGER PRIMARY KEY AUTOINCREMENT,
location TEXT,
uthmani TEXT,
simple TEXT,
translation TEXT,
transliteration TEXT,
audio_blob BLOB,
locations INT
);

INSERT INTO target_db.words 
(location,
uthmani,
simple,
translation,
transliteration,
audio_blob,
locations)
select
max(location) as location, 
--max(REPLACE(uthmani, '۞ ', '')) as uthmani, 
max(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(uthmani, '۞ ', ''), 'ۙ', ''), 'ۖ', ''), 'ۛ', ''), 'ۚ', '')) as uthmani, 
simple, 
max(translation) as translation, 
max(transliteration) as transliteration, 
max(audio_blob) as audio_blob, 
count(location) as locations
from main.words 
where length(simple) > 4
group by simple 
order by locations desc
limit 1000;
;

DETACH DATABASE target_db;

.quit

--- 121M alkalimah_words_2k.db
--- 62M alkalimah_words_1k.db

```

