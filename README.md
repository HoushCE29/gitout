# Git-Out
A script to speed up cleaning out old local branches for git repositories.

## Building
To build the source code and scripts, simply run:
```
gradlew clean build
```

The resulting zipped scripts are generated to:
```
/build/distributions
```

It's not recommended to run this via Gradle, nor via the standalone `jar`.

## Running
After building and unzipping the distribution, the scripts can be used. It's recommended to add the absolute path to `bin/gitout` to the system's path environment variable.

To simply run a basic cleaning, navigate to your repository directory and run:
```
gitout
```

If you wish to remove branches regardless of them being merged with the remote branch, then run with the `mergedOnly` flag:
```
gitout --mergedOnly=false
```
By default, the `mergedOnly` flag is set to `true`. When this flag is set to `true` and the branch has a remote counterpart, the branch will only be removed if the remote is in sync with the local copy.

If you want to retain specific branches, then run with the exclude flag:
```
gitout --exclude=[branch-to-keep1,branch-to-keep2,branch-to-keep3]
```
The "active" branch and `master` branch are always excluded.

Of course, all of these flags can be appended at once:
```
gitout --mergedOnly=false --exclude=[branch-to-keep]
```
