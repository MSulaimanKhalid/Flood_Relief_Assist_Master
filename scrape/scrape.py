import requests
from bs4 import BeautifulSoup
import pandas as pd
import re
import os

stationWeGottaKeep=["Mangla Dam","Marala","Trimmu","Panjnad","Tarbela Dam","KABUL","Kalabagh","Chashma","Taunsa","Guddu","Sukkur","Kotri"]

url = "https://ffd.pmd.gov.pk/river-state?zoom=6"

resp = requests.get(url, timeout=15)
print(resp)
resp.raise_for_status()
html = resp.text

tables = re.findall(r'(<table\b.*?</table>)', html, flags=re.DOTALL | re.IGNORECASE)
print("Tables Found: ", len(tables))

records = []
for t in tables:
    soup = BeautifulSoup(t, "html.parser")
    idx = html.find(t)

    station_title = None
    if idx != -1:
        context_start = max(0, idx - 500)
        context = html[context_start: idx + len(t)]
        m = re.search(r'<h[3-6][^>]*>([^<]+)</h[3-6]>', context, flags=re.IGNORECASE)
        if m:
            station_title = m.group(1).strip()

    if station_title not in stationWeGottaKeep:
        continue

    data = {"Station": station_title}

    for tr in soup.find_all('tr'):
        cols = tr.find_all(['td','th'])
        if len(cols) >= 2:
            key = cols[0].get_text(" ", strip=True)
            val = cols[1].get_text(" ", strip=True)
            data[key] = val

    if any(k.lower() in ['latitude','longitude','outflow','discharge','station height'] for k in data.keys()):
        records.append(data)

# Convert to DataFrame
new_df = pd.DataFrame.from_records(records)

# ------------ DUPLICATE CHECK ---------------
output_file = "river_state_extracted.csv"

if os.path.exists(output_file):
    old_df = pd.read_csv(output_file)

    # compare LAST READING of first station (e.g., "Mangla Dam")
    new_last_reading = new_df.iloc[0]["Last Reading at"]
    old_last_reading = old_df.iloc[-len(new_df)]["Last Reading at"]  # matches pattern

    if new_last_reading == old_last_reading:
        print("⛔ No new data found — not appending.")
        exit()
else:
    old_df = pd.DataFrame()

# ------------ APPEND ---------------
final_df = pd.concat([old_df, new_df], ignore_index=True)
final_df.to_csv(output_file, index=False)

print("✅ New data appended. Total rows:", len(final_df))
print(final_df.tail(25).to_string(index=False))
