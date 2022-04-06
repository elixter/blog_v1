package storage

type LocalImageStorageImpl struct {
	imageDir string
}

func New(imageDir string) *LocalImageStorageImpl {
	instance := &LocalImageStorageImpl{
		imageDir: imageDir,
	}

	return instance
}

func (l LocalImageStorageImpl) Remove(url string) error {
	panic("implement me")
}
