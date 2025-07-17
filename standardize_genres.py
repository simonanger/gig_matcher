#!/usr/bin/env python3
"""
Script to standardize genre names in the UK bands CSV file.
Applies comprehensive genre standardizations based on analysis results.
"""

import csv
import re
from typing import Dict, List

def create_genre_standardization_map() -> Dict[str, str]:
    """Create a comprehensive mapping of genre standardizations."""
    
    # Core standardizations based on analysis
    standardizations = {
        # Standalone metal genres that need "Metal" suffix
        'Black': 'Black Metal',
        'Death': 'Death Metal',
        'Doom': 'Doom Metal',
        'Thrash': 'Thrash Metal',
        'Power': 'Power Metal',
        'Heavy': 'Heavy Metal',
        'Folk': 'Folk Metal',
        'Gothic': 'Gothic Metal',
        'Industrial': 'Industrial Metal',
        'Pagan': 'Pagan Metal',
        'Viking': 'Viking Metal',
        'Stoner': 'Stoner Metal',
        'Sludge': 'Sludge Metal',
        
        # Descriptive terms that typically refer to metal
        'Progressive': 'Progressive Metal',
        'Symphonic': 'Symphonic Metal',
        'Technical': 'Technical Death Metal',  # Usually refers to technical death metal
        'Melodic': 'Melodic Metal',
        'Atmospheric': 'Atmospheric Metal',
        'Experimental': 'Experimental Metal',
        'Avant-garde': 'Avant-garde Metal',
        'Epic': 'Epic Metal',
        'Ambient': 'Ambient Metal',
        'Brutal': 'Brutal Death Metal',  # Usually refers to brutal death metal
        
        # Compound incomplete genres
        'Atmospheric Black': 'Atmospheric Black Metal',
        'Industrial Death': 'Industrial Death Metal',
        'Melodic Death': 'Melodic Death Metal',
        'Technical Death': 'Technical Death Metal',
        'Symphonic Black': 'Symphonic Black Metal',
        'Progressive Death': 'Progressive Death Metal',
        'Progressive Black': 'Progressive Black Metal',
        'Melodic Black': 'Melodic Black Metal',
        'Industrial Black': 'Industrial Black Metal',
        'Experimental Black': 'Experimental Black Metal',
        'Avant-garde Black': 'Avant-garde Black Metal',
        'Progressive Doom': 'Progressive Doom Metal',
        'Melodic Doom': 'Melodic Doom Metal',
        'Atmospheric Doom': 'Atmospheric Doom Metal',
        'Psychedelic Doom': 'Psychedelic Doom Metal',
        'Progressive Heavy': 'Progressive Heavy Metal',
        'Melodic Heavy': 'Melodic Heavy Metal',
        'Progressive Thrash': 'Progressive Thrash Metal',
        'Melodic Thrash': 'Melodic Thrash Metal',
        'Technical Thrash': 'Technical Thrash Metal',
        'Progressive Power': 'Progressive Power Metal',
        'Symphonic Power': 'Symphonic Power Metal',
        'Symphonic Death': 'Symphonic Death Metal',
        'Progressive Sludge': 'Progressive Sludge Metal',
        'Atmospheric Sludge': 'Atmospheric Sludge Metal',
        'Experimental Sludge': 'Experimental Sludge Metal',
        'Psychedelic Sludge': 'Psychedelic Sludge Metal',
        'Progressive Stoner': 'Progressive Stoner Metal',
        'Experimental Stoner': 'Experimental Stoner Metal',
        'Psychedelic Stoner': 'Psychedelic Stoner Metal',
        'Progressive Groove': 'Progressive Groove Metal',
        'Industrial Groove': 'Industrial Groove Metal',
        'Melodic Groove': 'Melodic Groove Metal',
        'Symphonic Folk': 'Symphonic Folk Metal',
        'Atmospheric Folk': 'Atmospheric Folk Metal',
        
        # Rock standardizations
        'Psychedelic': 'Psychedelic Rock',
        'Alternative': 'Alternative Rock',
        'Indie': 'Indie Rock',
        'Math': 'Math Rock',
        
        # Special cases and compound rock genres
        'Progressive Rock': 'Progressive Rock',  # Keep as is
        'Alternative Rock': 'Alternative Rock',  # Keep as is
        'Hard Rock': 'Hard Rock',  # Keep as is
        'Post Rock': 'Post-Rock',  # Standardize hyphenation
        'Atmospheric Rock': 'Atmospheric Rock',  # Keep as is
        'Industrial Rock': 'Industrial Rock',  # Keep as is
        'Progressive Post-Rock': 'Progressive Post-Rock',
        'Atmospheric Alternative Rock': 'Atmospheric Alternative Rock',
        'Experimental Psychedelic Doom': 'Experimental Psychedelic Doom Metal',
        
        # Electronic/ambient genres
        'Ambient': 'Ambient',  # Could be ambient music or ambient metal - keep context
        'Noise': 'Noise',  # Keep as is
        'Drone': 'Drone',  # Keep as is
        'Dark Ambient': 'Dark Ambient',  # Keep as is
        'Power Electronics': 'Power Electronics',  # Keep as is
        'Experimental Industrial': 'Experimental Industrial',
        'Atmospheric Industrial': 'Atmospheric Industrial',
        'Progressive Ambient': 'Progressive Ambient',
        'Psychedelic Drone': 'Psychedelic Drone',
        
        # Core genres - these are complete as they are
        'Deathcore': 'Deathcore',
        'Metalcore': 'Metalcore',
        'Grindcore': 'Grindcore',
        'Hardcore': 'Hardcore',
        'Mathcore': 'Mathcore',
        'Blackcore': 'Blackcore',  # Keep as is - it's a legitimate subgenre
        'Doomcore': 'Doomcore',   # Keep as is
        
        # Compound core genres
        'Melodic Deathcore': 'Melodic Deathcore',
        'Technical Deathcore': 'Technical Deathcore',
        'Progressive Deathcore': 'Progressive Deathcore',
        'Symphonic Deathcore': 'Symphonic Deathcore',
        'Brutal Deathcore': 'Brutal Deathcore',
        
        # Punk genres
        'Punk': 'Punk',  # Keep as is
        'Crust': 'Crust Punk',
        'D-Beat': 'D-Beat',  # Keep as is
        'Grind': 'Grindcore',  # Usually refers to grindcore
        
        # Special variants
        'Nu-Metal': 'Nu-Metal',  # Keep hyphenated
        'Slam': 'Slam',  # Keep as is (brutal death metal subgenre)
        'Blackened Death Metal': 'Blackened Death Metal',  # Keep as is
        'Blackened Thrash Metal': 'Blackened Thrash Metal',  # Keep as is
        'Blackened Deathcore': 'Blackened Deathcore',  # Keep as is
        
        # Roll variants - these are complete subgenres
        "Black 'n' Roll": "Black 'n' Roll",
        "Death 'n' Roll": "Death 'n' Roll", 
        "Thrash 'n' Roll": "Thrash 'n' Roll",
        
        # Post- genres (keep hyphenation consistent)
        'Post-Metal': 'Post-Metal',
        'Post-Rock': 'Post-Rock',
        'Post-Punk': 'Post-Punk',
        'Post-Hardcore': 'Post-Hardcore',
        'Post-Black Metal': 'Post-Black Metal',
        'Avant-garde Post-Black': 'Avant-garde Post-Black Metal',
    }
    
    return standardizations

def standardize_genre_string(genre_string: str, standardization_map: Dict[str, str]) -> str:
    """Standardize a genre string that may contain multiple genres separated by delimiters."""
    if not genre_string.strip():
        return genre_string
    
    # Split by common delimiters
    delimiters = ['/', ',', ';']
    parts = [genre_string]
    
    for delimiter in delimiters:
        new_parts = []
        for part in parts:
            new_parts.extend(part.split(delimiter))
        parts = new_parts
    
    # Clean and standardize each part
    standardized_parts = []
    for part in parts:
        cleaned_part = part.strip()
        if cleaned_part:
            # Apply standardization if exists
            if cleaned_part in standardization_map:
                standardized_parts.append(standardization_map[cleaned_part])
            else:
                standardized_parts.append(cleaned_part)
    
    # Rejoin with consistent delimiter
    return '/'.join(standardized_parts)

def process_csv_file(input_file: str, output_file: str) -> None:
    """Process the CSV file and apply genre standardizations."""
    
    standardization_map = create_genre_standardization_map()
    
    changes_made = {}
    total_rows = 0
    rows_changed = 0
    
    with open(input_file, 'r', encoding='utf-8') as infile, \
         open(output_file, 'w', encoding='utf-8', newline='') as outfile:
        
        reader = csv.DictReader(infile)
        fieldnames = reader.fieldnames
        writer = csv.DictWriter(outfile, fieldnames=fieldnames)
        
        writer.writeheader()
        
        for row in reader:
            total_rows += 1
            original_genre = row.get('genre', '').strip()
            
            if original_genre:
                standardized_genre = standardize_genre_string(original_genre, standardization_map)
                
                if standardized_genre != original_genre:
                    rows_changed += 1
                    if original_genre not in changes_made:
                        changes_made[original_genre] = []
                    changes_made[original_genre].append(standardized_genre)
                
                row['genre'] = standardized_genre
            
            writer.writerow(row)
    
    # Print summary
    print(f"\nStandardization Complete!")
    print(f"Total rows processed: {total_rows}")
    print(f"Rows with genre changes: {rows_changed}")
    print(f"Unique genre patterns changed: {len(changes_made)}")
    
    # Show changes made
    if changes_made:
        print("\nGenre Standardizations Applied:")
        print("-" * 50)
        for original, standardized_list in sorted(changes_made.items()):
            # Get the most common standardization (they should all be the same)
            standardized = standardized_list[0]
            count = len(standardized_list)
            print(f"  '{original}' → '{standardized}' ({count} occurrences)")
    
    # Save change log
    with open('genre_changes_log.txt', 'w') as f:
        f.write("Genre Standardization Changes Log\n")
        f.write("=" * 40 + "\n\n")
        f.write(f"Total rows processed: {total_rows}\n")
        f.write(f"Rows with genre changes: {rows_changed}\n")
        f.write(f"Unique genre patterns changed: {len(changes_made)}\n\n")
        
        for original, standardized_list in sorted(changes_made.items()):
            standardized = standardized_list[0]
            count = len(standardized_list)
            f.write(f"'{original}' → '{standardized}' ({count} occurrences)\n")

def main():
    """Main function to run the standardization process."""
    input_file = 'uk_active_bands_cleaned.csv'
    output_file = 'uk_active_bands_standardized.csv'
    
    print("Starting genre standardization process...")
    print(f"Input file: {input_file}")
    print(f"Output file: {output_file}")
    
    try:
        process_csv_file(input_file, output_file)
        print(f"\nStandardized data saved to: {output_file}")
        print("Change log saved to: genre_changes_log.txt")
        
    except FileNotFoundError:
        print(f"Error: Input file '{input_file}' not found.")
        print("Please ensure the file exists in the current directory.")
    except Exception as e:
        print(f"Error during processing: {e}")

if __name__ == "__main__":
    main()