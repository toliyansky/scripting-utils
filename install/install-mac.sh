echo "Starting installation scripting-utils v22.0.0"

INSTALL_DIR=~/scripting-utils
echo "Installation directory: $INSTALL_DIR"

echo "Checking Java." &&
if ! [ -x "$(command -v java)" ]; then
  echo "Error: Java is not installed." >&2
  exit 1
fi
echo "Removing old version if exist." &&
rm -rf $INSTALL_DIR &&
echo "Creating installation directory." &&
mkdir -p $INSTALL_DIR/scripting &&
echo "Download sources." &&
curl -s https://raw.githubusercontent.com/AnatoliyKozlov/scripting-utils/master/src/scripting/Utils.java > $INSTALL_DIR/scripting/Utils.java &&
echo "Compile sources." &&
javac --source 22 --enable-preview $INSTALL_DIR/scripting/Utils.java &&
echo "Installation completed."

RESET='\033[0m'
BLUE='\033[34m'
CYAN='\033[36m'
GREEN='\033[32m'
YELLOW='\033[33m'
MAGENTA='\033[35m'

echo -e "\nExample of Java shebang script:"
echo -e "---------------------------------------------------------------------"
echo -e "${YELLOW}#!/usr/bin/java --source 22 --enable-preview --class-path $INSTALL_DIR${YELLOW}${RESET}\n"
echo -e "${BLUE}import${RESET} ${CYAN}static scripting.Utils${RESET}.${MAGENTA}*${RESET};\n"
echo -e "${BLUE}void main${RESET}() {"
echo -e "    ${CYAN}var response${RESET} = ${CYAN}http${RESET}.${GREEN}get${RESET}(\"${MAGENTA}https://httpbin.org/get${RESET}\");"
echo -e "    ${CYAN}log${RESET}.${GREEN}info${RESET}(response.${GREEN}body${RESET}());"
echo -e "}"
echo -e "---------------------------------------------------------------------"
echo -e "Don't forget apply chmod +x for your shebang\n"