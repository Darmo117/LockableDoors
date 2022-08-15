import pathlib
import sys

file_pattern = sys.argv[1]
to_replace = sys.argv[2]
replacer = sys.argv[3]

for p in pathlib.Path('src/main/resources/assets').glob(file_pattern):
    file_name = p.name
    new_file = p.parent / p.name.replace(to_replace, replacer)
    with p.open('r') as f:
        print(f'Converting {p}…')
        with new_file.open('w') as out:
            for line in f.readlines():
                out.write(line.replace(to_replace, replacer))
print('Done')
