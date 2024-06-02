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
echo "Replace sources." &&
cp ../src/scripting/Utils.java $INSTALL_DIR/scripting/Utils.java &&
echo "Compile sources." &&
javac --source 22 --enable-preview $INSTALL_DIR/scripting/Utils.java &&
echo "Installation completed."