import {Component, Injectable, OnInit} from '@angular/core';
import {Product} from '../../model/Product';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {NzTableModule} from 'ng-zorro-antd/table';
import {delay} from 'rxjs/operators';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css']
})

export class WelcomeComponent implements OnInit {
  readModalIsVisible = false;
  updateModalIsVisible = false;
  createModalIsVisible = false;

  productsList: Product[] = [];
  productToShow: Product;
  productToEdit: Product;

  name: '';
  description: '';
  price: 0;
  url: '';

  urlOfEditingProduct: '';

  proxyUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {
  }

  ngOnInit(): void {
    this.getProductsList();
  }

  private list(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.proxyUrl}/product/list`);
  }

  // tslint:disable-next-line:typedef
  private getProductsList() {
    this.list().subscribe(
      response => {
        this.productsList = response;
        this.productToShow = this.productsList[0];
        this.productToEdit = this.productsList[0];
      },
      error => {
        console.log('Error on getting product list.');
      });
  }

  private create(name: string, description: string, price: number, url: string): Observable<Product> {

    // @ts-ignore
    return this.http.put<Product>(`${this.proxyUrl}/product/create?name=${name}
                                                                          &description=${description}
                                                                          &price=${price}
                                                                          &url=${url}`);
  }

  // tslint:disable-next-line:typedef
  private createProduct(name: string, description: string, price: number, url: string) {
    this.create(name, description, price, url).subscribe(
      response => {
        this.productsList.push(response);
      },
      error => {
        console.log('Error on creating product.');
      });
  }

  private read(url: string): Observable<Product> {
    const httpParams = new HttpParams()
      .set('url', url);

    const options = {params: httpParams};

    return this.http.get<Product>(`${this.proxyUrl}/product/read`, options);
  }

  // tslint:disable-next-line:typedef
  private readProduct(url: string) {
    this.read(url).subscribe(
      response => {
        this.productToShow = response;
      },
      error => {
        console.log('Error on reading product.');
      });
  }

  private update(product: Product, urlOfEditingProduct: string): Observable<Product> {
    // @ts-ignore
    return this.http.put<Product>(`${this.proxyUrl}/product/update?name=${product.name}
                                                                          &description=${product.description}
                                                                          &price=${product.price}
                                                                          &url=${urlOfEditingProduct}
                                                                          &newUrl=${product.url}`);
  }

  // tslint:disable-next-line:typedef
  private updateProduct(product: Product, urlOfEditingProduct: string) {
    this.update(product, urlOfEditingProduct).subscribe(
      response => {
        this.productToShow = response;
      },
      error => {
        console.log('Error on updating product.');
      });
  }

  private delete(url: string): Observable<Product> {
    const httpParams = new HttpParams()
      .set('url', url);

    const options = {params: httpParams};
    return this.http.delete<Product>(`${this.proxyUrl}/product/delete`, options);
  }

  // tslint:disable-next-line:typedef
  deleteProduct(url: string) {
    this.delete(url).subscribe();
    this.getProductsList();
  }

  readModalShow(product: Product): void {
    this.readProduct(product.url);
    this.readModalIsVisible = true;
  }

  readModalHandleOk(): void {
    this.readModalIsVisible = false;
  }

  readModalHandleCancel(): void {
    this.readModalIsVisible = false;
  }

  createModalShow(): void {
    this.name = '';
    this.description = '';
    this.price = 0;
    this.url = '';

    this.createModalIsVisible = true;
  }

  async createModalHandleOk(): Promise<void> {
    this.createModalIsVisible = false;

    this.createProduct(this.name, this.description, this.price, this.url);
    this.productsList.push(new Product(this.name, this.description, this.price, this.url));
  }

  createModalHandleCancel(): void {
    this.createModalIsVisible = false;
  }

  updateModalShow(product): void {
    this.productToEdit = product;
    this.urlOfEditingProduct = product.url;
    this.updateModalIsVisible = true;
  }

  updateModalHandleOk(): void {
    this.updateProduct(this.productToEdit, this.urlOfEditingProduct);
    this.updateModalIsVisible = false;
  }

  updateModalHandleCancel(): void {
    this.updateModalIsVisible = false;
  }
}
