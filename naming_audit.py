import re
from pathlib import Path
root = Path('src/main/java')
violations = []
for path in root.rglob('*.java'):
    text = path.read_text(encoding='utf-8')
    m = re.search(r'^package\s+([\w.]+);', text, re.MULTILINE)
    if m:
        pkg = m.group(1)
        if any(c.isupper() for c in pkg) or '_' in pkg:
            violations.append((str(path), 'package', m.start(1) + 1, pkg, 'package name should be all lowercase with dots only'))
    lines = text.splitlines()
    for i, line in enumerate(lines, 1):
        m = re.search(r'\b(class|interface|enum)\s+([A-Za-z_][A-Za-z0-9_]*)', line)
        if m:
            name = m.group(2)
            if not re.match(r'[A-Z][A-Za-z0-9]*$', name):
                violations.append((str(path), 'class', i, name, 'class/interface/enum name should be PascalCase'))
        m = re.search(r'\b(@?)(?:public|protected|private)?\s*(?:static\s+)?(?:final\s+)?(?:synchronized\s+)?(?:[A-Za-z_][A-Za-z0-9_<>, \[\]]+)\s+([A-Za-z_][A-Za-z0-9_]*)\s*\(', line)
        if m and not line.strip().startswith('@'):
            name = m.group(2)
            if name in ('doGet', 'doPost', 'hashCode', 'equals', 'toString', 'init', 'destroy', 'doFilter'):
                continue
            if not re.match(r'[a-z][A-Za-z0-9]*$', name):
                violations.append((str(path), 'method', i, name, 'method name should be camelCase'))

for v in violations:
    print(f'{v[0]}:{v[2]}: {v[1]} "{v[3]}" - {v[4]}')
