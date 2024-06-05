echo "$OSTYPE"

if [[ "$OSTYPE" == "linux-gnu"* ]]; then
  echo "$OSTYPE not supported yet."
elif [[ "$OSTYPE" == "darwin"* ]]; then
  echo "Start installing for $OSTYPE."
  curl -s https://raw.githubusercontent.com/AnatoliyKozlov/scripting-utils/master/install/install-mac.sh | bash
elif [[ "$OSTYPE" == "win32" ]]; then
  echo "$OSTYPE not supported yet."
else
  echo "$OSTYPE not supported yet."
fi

# TODO: Add more OS types. cygwin/msys/freebsd etc.