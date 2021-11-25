# My ZShell Extensions

A collection of zsh extensions and functions to make my life easier.

## A note on dependencies

The following must be installed for these scripts to work as is:

1. Android SDK (if you install the [android-dev-support](android-dev-support.zsh) script)
2. Python 3
3. [Exa](https://github.com/ogham/exa)  : powered up version of the humble `ls` command.

## How to hook into these scripts

Clone the this repo to `~/.my-zsh`:

```shell
git clone git@github.com:andriesfc/my-zsh.git "$HOME/.my-zsh"
```

Add these lines tou your `~/.zshrc` file:

```shell
source "$HOME/.my-zsh/functions.zsh"
source "$HOME/.my-zsh/android-dev-support.zsh"
```

## Current Functions

| Function                           | Description                                                   |
| ---------------------------------- | ------------------------------------------------------------- |
| `date -I`                          | Displays date in ISO format                                   |
| `append_to_path`                   | Appends something to the path                                 |
| `current_dir_name`                 | Get the current directory name.                               |
| `lst ~/projects`                   | A ls command which displays more information in table format. |
| `drop ./test.json ./wip`           | Drops all files and folders.                                  |
| `resolvepath ~/../../Applications` | Resolves to `/Applications` on Mac                            |