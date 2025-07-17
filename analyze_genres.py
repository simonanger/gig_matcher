#!/usr/bin/env python3
"""
Script to analyze genres in the UK bands CSV file and identify incomplete genre names.
"""

import csv
import re
from collections import Counter
from typing import Set, List, Dict

def extract_genres_from_csv(filename: str) -> List[str]:
    """Extract all genres from the CSV file."""
    genres = []
    
    with open(filename, 'r', encoding='utf-8') as file:
        reader = csv.DictReader(file)
        for row in reader:
            genre_field = row.get('genre', '').strip()
            if genre_field:
                # Split by common separators and clean up
                genre_parts = re.split(r'[/,;]', genre_field)
                for part in genre_parts:
                    cleaned_genre = part.strip()
                    if cleaned_genre:
                        genres.append(cleaned_genre)
    
    return genres

def analyze_incomplete_genres(genres: List[str]) -> Dict[str, List[str]]:
    """Analyze genres to find incomplete patterns."""
    unique_genres = set(genres)
    genre_counts = Counter(genres)
    
    # Categories of incomplete genres
    incomplete_patterns = {
        'incomplete_metal': [],
        'incomplete_rock': [],
        'incomplete_core': [],
        'incomplete_punk': [],
        'incomplete_other': []
    }
    
    # Keywords that often indicate incomplete genres
    metal_indicators = ['Black', 'Death', 'Doom', 'Thrash', 'Power', 'Progressive', 'Symphonic', 
                       'Technical', 'Melodic', 'Brutal', 'Atmospheric', 'Industrial', 'Gothic',
                       'Folk', 'Viking', 'Pagan', 'Experimental', 'Avant-garde', 'Post']
    
    rock_indicators = ['Progressive', 'Psychedelic', 'Alternative', 'Indie', 'Post', 'Math']
    
    core_indicators = ['Death', 'Metal', 'Math', 'Post', 'Grind', 'Doom', 'Black', 'Sludge']
    
    punk_indicators = ['Crust', 'Hardcore', 'Post', 'Pop']
    
    for genre in unique_genres:
        # Check for incomplete metal genres
        for indicator in metal_indicators:
            if genre == indicator or (genre.startswith(indicator + ' ') and 'Metal' not in genre):
                incomplete_patterns['incomplete_metal'].append(genre)
                break
        
        # Check for incomplete rock genres
        for indicator in rock_indicators:
            if genre == indicator or (genre.startswith(indicator + ' ') and 'Rock' not in genre and 'Metal' not in genre):
                incomplete_patterns['incomplete_rock'].append(genre)
                break
        
        # Check for incomplete core genres
        if genre.endswith('core') and len(genre) > 4:
            base = genre[:-4].strip()
            if base in core_indicators:
                incomplete_patterns['incomplete_core'].append(genre)
        
        # Check for standalone descriptive words that might be incomplete
        standalone_words = ['Technical', 'Melodic', 'Brutal', 'Atmospheric', 'Industrial', 
                           'Gothic', 'Symphonic', 'Epic', 'Ambient', 'Experimental']
        if genre in standalone_words:
            incomplete_patterns['incomplete_other'].append(genre)
    
    return incomplete_patterns, genre_counts

def suggest_standardizations(incomplete_patterns: Dict[str, List[str]], 
                           genre_counts: Counter) -> Dict[str, str]:
    """Suggest standardizations for incomplete genres."""
    standardizations = {}
    
    # Metal standardizations
    metal_standards = {
        'Atmospheric Black': 'Atmospheric Black Metal',
        'Industrial Death': 'Industrial Death Metal',
        'Progressive': 'Progressive Metal',
        'Symphonic': 'Symphonic Metal',
        'Technical': 'Technical Death Metal',
        'Melodic Death': 'Melodic Death Metal',
        'Brutal Death': 'Brutal Death Metal',
        'Gothic': 'Gothic Metal',
        'Folk': 'Folk Metal',
        'Viking': 'Viking Metal',
        'Pagan': 'Pagan Metal',
        'Doom': 'Doom Metal',
        'Black': 'Black Metal',
        'Death': 'Death Metal',
        'Thrash': 'Thrash Metal',
        'Power': 'Power Metal',
        'Industrial': 'Industrial Metal',
        'Atmospheric': 'Atmospheric Metal',
        'Experimental': 'Experimental Metal',
        'Avant-garde': 'Avant-garde Metal',
        'Post': 'Post-Metal',
        'Epic': 'Epic Metal',
        'Ambient': 'Ambient Metal'
    }
    
    # Rock standardizations
    rock_standards = {
        'Progressive Rock': 'Progressive Rock',  # Keep as is
        'Psychedelic': 'Psychedelic Rock',
        'Alternative': 'Alternative Rock',
        'Indie': 'Indie Rock',
        'Post Rock': 'Post-Rock',
        'Math': 'Math Rock'
    }
    
    # Core standardizations
    core_standards = {
        'Deathcore': 'Deathcore',  # Keep as is
        'Metalcore': 'Metalcore',  # Keep as is
        'Mathcore': 'Mathcore',   # Keep as is
        'Grindcore': 'Grindcore', # Keep as is
        'Hardcore': 'Hardcore',   # Keep as is
        'Doomcore': 'Doom Metal', # Convert to doom metal
        'Blackcore': 'Black Metal' # Convert to black metal
    }
    
    # Apply standardizations
    for genre in incomplete_patterns['incomplete_metal']:
        if genre in metal_standards:
            standardizations[genre] = metal_standards[genre]
    
    for genre in incomplete_patterns['incomplete_rock']:
        if genre in rock_standards:
            standardizations[genre] = rock_standards[genre]
    
    for genre in incomplete_patterns['incomplete_other']:
        if genre in metal_standards:
            standardizations[genre] = metal_standards[genre]
        elif genre in rock_standards:
            standardizations[genre] = rock_standards[genre]
    
    return standardizations

def main():
    """Main analysis function."""
    print("Analyzing genres in UK bands CSV file...")
    
    # Extract genres
    genres = extract_genres_from_csv('uk_active_bands_cleaned.csv')
    print(f"Total genre entries found: {len(genres)}")
    
    # Analyze incomplete genres
    incomplete_patterns, genre_counts = analyze_incomplete_genres(genres)
    
    print("\n" + "="*50)
    print("GENRE ANALYSIS RESULTS")
    print("="*50)
    
    # Show top genres
    print("\nTop 20 most common genres:")
    for genre, count in genre_counts.most_common(20):
        print(f"  {genre}: {count}")
    
    # Show incomplete patterns
    print("\nIncomplete Metal Genres:")
    for genre in sorted(incomplete_patterns['incomplete_metal']):
        count = genre_counts[genre]
        print(f"  {genre}: {count} occurrences")
    
    print("\nIncomplete Rock Genres:")
    for genre in sorted(incomplete_patterns['incomplete_rock']):
        count = genre_counts[genre]
        print(f"  {genre}: {count} occurrences")
    
    print("\nIncomplete Core Genres:")
    for genre in sorted(incomplete_patterns['incomplete_core']):
        count = genre_counts[genre]
        print(f"  {genre}: {count} occurrences")
    
    print("\nOther Incomplete Genres:")
    for genre in sorted(incomplete_patterns['incomplete_other']):
        count = genre_counts[genre]
        print(f"  {genre}: {count} occurrences")
    
    # Generate standardizations
    standardizations = suggest_standardizations(incomplete_patterns, genre_counts)
    
    print("\n" + "="*50)
    print("SUGGESTED STANDARDIZATIONS")
    print("="*50)
    
    for old_genre, new_genre in sorted(standardizations.items()):
        count = genre_counts[old_genre]
        print(f"  '{old_genre}' → '{new_genre}' ({count} occurrences)")
    
    # Save standardizations to file
    with open('genre_standardizations.txt', 'w') as f:
        f.write("Genre Standardizations Mapping\n")
        f.write("="*40 + "\n\n")
        for old_genre, new_genre in sorted(standardizations.items()):
            count = genre_counts[old_genre]
            f.write(f"'{old_genre}' → '{new_genre}' ({count} occurrences)\n")
    
    print(f"\nStandardizations saved to 'genre_standardizations.txt'")
    print(f"Total standardizations suggested: {len(standardizations)}")

if __name__ == "__main__":
    main()