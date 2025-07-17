#!/usr/bin/env python3
"""
Script to clean the UK active bands CSV file.
Processes the genre field by splitting, cleaning, and deduplicating genres.
"""

import csv
import re
from typing import List, Set


def clean_genre_field(genre: str) -> str:
    """
    Clean the genre field by:
    - Splitting genres by '/', ',', and ';'
    - Removing anything in parentheses like (early), (later), (mid)
    - Trimming whitespace
    - Filtering out empty strings
    - Deduplicating genres
    - Joining cleaned genres back with '/'
    """
    if not genre or genre.strip() == "":
        return ""
    
    # Split by '/', ',', and ';'
    separators = ['/', ',', ';']
    genres = [genre]
    
    for separator in separators:
        temp_genres = []
        for g in genres:
            temp_genres.extend(g.split(separator))
        genres = temp_genres
    
    # Clean each genre
    cleaned_genres: Set[str] = set()
    
    for g in genres:
        # Remove content in parentheses
        cleaned = re.sub(r'\([^)]*\)', '', g)
        # Trim whitespace
        cleaned = cleaned.strip()
        # Only add non-empty genres
        if cleaned:
            cleaned_genres.add(cleaned)
    
    # Sort for consistent output and join with '/'
    return '/'.join(sorted(cleaned_genres))


def main():
    """Main function to process the CSV file."""
    input_file = "/Users/simonanger/AndroidStudioProjects/GigMatcher/app/src/main/assets/uk_active_bands.csv"
    output_file = "/Users/simonanger/AndroidStudioProjects/GigMatcher/uk_active_bands_cleaned.csv"
    
    print(f"Reading from: {input_file}")
    print(f"Writing to: {output_file}")
    
    rows_processed = 0
    
    try:
        with open(input_file, 'r', encoding='utf-8') as infile, \
             open(output_file, 'w', encoding='utf-8', newline='') as outfile:
            
            reader = csv.DictReader(infile)
            fieldnames = reader.fieldnames
            
            if not fieldnames:
                print("Error: Could not read CSV headers")
                return
            
            writer = csv.DictWriter(outfile, fieldnames=fieldnames)
            writer.writeheader()
            
            for row in reader:
                # Clean the genre field
                if 'genre' in row:
                    original_genre = row['genre']
                    cleaned_genre = clean_genre_field(original_genre)
                    row['genre'] = cleaned_genre
                    
                    # Print progress every 500 rows
                    if rows_processed % 500 == 0:
                        print(f"Processed {rows_processed} rows...")
                        if rows_processed < 5:  # Show first few examples
                            print(f"  Example - Original: '{original_genre}' -> Cleaned: '{cleaned_genre}'")
                
                writer.writerow(row)
                rows_processed += 1
            
            print(f"Successfully processed {rows_processed} rows")
            print(f"Cleaned CSV written to: {output_file}")
    
    except FileNotFoundError:
        print(f"Error: Input file not found: {input_file}")
    except Exception as e:
        print(f"Error processing file: {e}")


if __name__ == "__main__":
    main()