# Project Documentation Rules (Non-Obvious Only)

- **Interview preparation focus** - Project designed as coding interview demonstration, not production system
- **Design patterns showcase** - Architecture intentionally over-engineered to demonstrate Strategy, Factory, Observer, Singleton patterns
- **Package structure by pattern** - Organized by design pattern type (strategies/, factories/, observers/) not business domain
- **Multiple documentation files** - README.md (comprehensive), INTERVIEW_CHEAT_SHEET.md (quick reference), SIMPLE_START_GUIDE.md (step-by-step), FLOW_DIAGRAMS.md (visual aids)
- **Main.java in root** - Despite being in root, imports from packages to demonstrate proper package structure
- **No external dependencies** - Pure Java implementation using only standard library (no Maven/Gradle)