import fs from "fs";
import path from "path";
import AdmZip from "adm-zip";
import {fileURLToPath} from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const exerciseName = process.argv[2];

if (!exerciseName) {
  console.error('Please provide the exercise name as an argument (e.g., exercise08).');
  process.exit(1);
}

const rootDir = path.join(__dirname, '..');
const sourceDir = path.join(rootDir, 'src', exerciseName);
const outputZipPath = path.join(rootDir, 'dist-zip', `${exerciseName}.zip`);

if (!fs.existsSync(sourceDir)) {
  console.error(`Source directory not found: ${sourceDir}`);
  process.exit(1);
}

try {
  const zip = new AdmZip();
  // Adds the exercise directory inside a 'src' folder in the zip
  zip.addLocalFolder(sourceDir, `src/${exerciseName}`);
  
  zip.writeZip(outputZipPath);
  console.log(`Successfully created ${outputZipPath}`);
} catch (error) {
  console.error(`Error creating zip file: ${error.message}`);
  process.exit(1);
}
