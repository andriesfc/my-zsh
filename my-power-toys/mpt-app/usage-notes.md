# Usage Notes

## Command Line Tools

```shell
mpt rename
mpt move 
mpt copy
mpt delete 
mpt daemon start
mpt daemon stop
mtp config set <key>=<value>
mtp config list 
mtp config drop <path-or-key> # drop mtp - Drops everything
```

## Configuration

Config follows (in order of precedence): 

- $CWD/.mpt.conf
- $HOME/.mpt.conf
- $MPT_INSTALL/mtp.conf

Configuration format is HOCON, for example:

```hocon
mtp {
  deamon {
    on-demand = true
    time-to-live = "2m"
  }
}
```