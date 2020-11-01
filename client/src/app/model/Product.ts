export class Product {
  id: string;
  name: string;
  description: string;
  price: number;
  url: string;

  // tslint:disable-next-line:typedef
  constructor(name: string, description: string, price: number, url: string) {
    this.name = name;
    this.description = description;
    this.price = price;
    this.url = url;
  }
}
