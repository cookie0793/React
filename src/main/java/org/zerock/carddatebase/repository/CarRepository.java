package org.zerock.carddatebase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.zerock.carddatebase.domain.Car;

import java.util.List;

/*

 Rest Repository 설정에 의해
 이 CarRepository는 josn Controller의 역할을 한다.
 1. 전체 조회 GET - http://localhost:12000/api/cars
 2. ID 3인 CAR 조회 GET - http://localhost:12000/api/cars/3
 3. ID 3인 CAR의 OWNER 조회 GET - http://localhost:12000/api/cars/3/owner
 4. 생성 POST - http://localhost:12000/api/cars
    Body > raw > JSON
    전송
    {
    "brand" : "Samsung",
    "model" : "SM-5",
    "color" : "black",
    "registerNumber" : "SSS-111",
    "year" : 2024,
    "price" : 3200
 }

    응답
    {
    "brand": "Samsung",
    "model": "SM-5",
    "color": "black",
    "registerNumber": "SSS-111",
    "year": 2024,
    "price": 3200,
    "_links": {
        "self": {
            "href": "http://localhost:12000/api/cars/6"
        },
        "car": {
            "href": "http://localhost:12000/api/cars/6"
        },
        "owner": {
            "href": "http://localhost:12000/api/cars/6/owner"
        }
    }
}
 5. 업데이트 PATCH - http://localhost:12000/api/cars/6
    Body > rar > JSON

    응답
    {
    "brand": "Samsung",
    "model": "SM-5",
    "color": "gold",
    "registerNumber": "SSS-111",
    "year": 2024,
    "price": 3200,
    "_links": {
        "self": {
            "href": "http://localhost:12000/api/cars/6"
        },
        "car": {
            "href": "http://localhost:12000/api/cars/6"
        },
        "owner": {
            "href": "http://localhost:12000/api/cars/6/owner"
        }
    }
}

6. ID 7인 CAR의 OWNER를 추가 PUT - http://localhost:12000/api/cars/7/owner
http://localhost:12000/api/owners/1

Content-Type : text/uri-list

Body > raw > Text
전송
http://localhost:12000/api/owners/1
응답
1

7. 추가적인 요청
GET - http://localhost:12000/api/cars/search

응답
{
    "_links": {
        "findByBrand": {
            "href": "http://localhost:12000/api/cars/search/findByBrand{?brand}",
            "templated": true
        },
        "findByColor": {
            "href": "http://localhost:12000/api/cars/search/findByColor{?color}",
            "templated": true
        },
        "self": {
            "href": "http://localhost:12000/api/cars/search"
        }
    }
}

8. color로 조회
GET - http://localhost:12000/api/cars/search/findByColor?color=white



*/

// @RepositoryRestResource -> 현재 Repository의 api 주소를 변경할 수 있다.
// @RepositoryRestResource(path = "aaa")

// Client에서 요청하는 추가 메서드를 만들려면 @RepositoryRestResource를 써주고
// 아래처럼 @Param으로 요청값을 전달해야 한다.
@RepositoryRestResource
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByBrand(@Param("brand") String brand);

    List<Car> findByColor(@Param("color") String color);
}
