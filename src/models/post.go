package models

import (
	"time"
	"regexp"	
	"database/sql"
	
	"github.com/grokify/html-strip-tags-go"
)

type Post struct {
	Id			int			`json: "id" db: "id" gorm: "id"`
	Author		string		`json: "author" db: "author" gorm: "author"`
	UDesc		string		`json: "udesc" db: "udesc" gorm: "udesc"`
	Title		string		`json: "title" db: "title" gorm: "title"`
	Thumbnail	string		`json: "thumbnail" db: "thumbnail" gorm: "thumbnail"`
	Content		string		`json: "content" db: "content" gorm: "content"`
	Summary		string		`json: "summary" db: "summary" gorm: "summary"`
	Date	time.Time	`json: "date" db: "date" gorm: "date"`
	Updated	time.Time	`json: "updated" db: "update" gorm: "updated"`
	Category string `json: category" db: "category" gorm: "category"`
	HashTags []string
}

type page struct {
	CurrentPage int
	Length int
}

// Helper Functions
func GetCategories(db *sql.DB) ([]string, error) {
	var categories []string

	// Database에서 카테고리 가져오기
	rows, err := db.Query("select category from categories;")
	if err != nil {
		log.Println(err)
	}

	for rows.Next() {
		var tmp string
		err = rows.Scan(&tmp)
		if err != nil {
			log.Println(err)
		}
		categories = append(categories, tmp)
	}

	return categories, err
}

// Member Functions
func (p Post) convertHashTag() []string {
	// "#해쉬태그 #테스트" 와 같은 문자열을
	// [해쉬태그, 테스트] 로 바꿔주는 함수
	regExp := regexp.MustCompile("#")

	regedHash := regExp.ReplaceAllLiteralString(hashTags, "")		// 정규화된 해쉬태그들

	return strings.Split(regedHash, " ")		// 공백으로 토큰화하여 리턴
}

func (p *Post) CreateSummary(summaryLength int) string {
	// Content에서 특정길이 문자열에서 html태그 제거한것.
	var sumText string
	
	if len(p.Content) >= summaryLength {
		if (strings.Contains(p.Content, "&nbsp;")) {
			sumText = strings.Split(p.Content, "&nbsp;")[0]
		} else {
			sumText = p.Content[:summaryLength]
		}
	}  else {
		if (strings.Contains(p.Content, "&nbsp;")) {
			sumText = strings.Split(p.Content, "&nbsp;")[0]
		} else {
			sumText = p.Content
		}
	}
	p.Summary = strip.StripTags(sumText)
	
	return p.Summary
}

func (p *Post) CreateThumbnail() string, error {
	var thumbnail string
	contentDoc, err := goquery.NewDocumentFromReader(strings.NewReader(content))
	if err != nil {
		log.Println(err)
	}
	firstImage := contentDoc.Find("img").First()
	if len(firstImage.Nodes) != 0 {
		thumbnail = firstImage.Nodes[0].Attr[src].Val
	} else {
		thumbnail = ""
	}
	
	p.Thumbnail = thumbnail

	return p.Thumbnail, error
}
