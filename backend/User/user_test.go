package User

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestServiceJoin(t *testing.T) {
	userService := ServiceImpl{
		Repository: &MemoryRepository{},
	}

	// given
	user := User{
		Id: 1,
		Name: "elixter",
		Grade: Admin,
	}

	// when
	err := userService.Join(user)
	if err != nil {
		t.Error(err)
	}

	// then
	userFromRepository, err := userService.FindUser(1)
	if err != nil {
		t.Error(err)
	}

	assert.Equal(t, user, userFromRepository)
}
