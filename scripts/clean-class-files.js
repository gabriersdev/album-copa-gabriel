import fs from "fs";
import path from "path";
import {fileURLToPath} from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const rootDir = path.join(__dirname, '..');

// Directories to ignore during traversal
const ignoreDirs = ['node_modules', '.git', '.idea', 'dist-zip', 'assets', 'docs'];

function deleteClassFiles(dir) {
  if (!fs.existsSync(dir)) return;
  
  const files = fs.readdirSync(dir);
  
  for (const file of files) {
    const fullPath = path.join(dir, file);
    const stat = fs.statSync(fullPath);
    
    if (stat.isDirectory()) {
      if (!ignoreDirs.includes(file)) {
        deleteClassFiles(fullPath);
      }
    } else if (file.endsWith('.class')) {
      try {
        fs.unlinkSync(fullPath);
        console.log(`Deleted: ${fullPath.replace(rootDir + path.sep, '')}`);
      } catch (error) {
        console.error(`Failed to delete ${fullPath}: ${error.message}`);
      }
    }
  }
}

console.log('Searching for and deleting .class files...');
deleteClassFiles(rootDir);
console.log('Cleanup complete.');
