for file in $(find . -type f -name "*.js"); do
    directory=$(dirname "$file")
    filename=$(basename "$file")
    new_filename="babel-$filename"
    new_filepath="$directory/$new_filename"
    babel "$file" --out-file "$new_filepath"
done
